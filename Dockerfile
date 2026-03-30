# Build stage
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copy gradle wrapper and build files
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

# Make gradlew executable
RUN chmod +x gradlew

# Copy source code
COPY src/ src/

# Build the application
RUN ./gradlew bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

# Create application user
RUN addgroup -g 1001 -S maodien && \
    adduser -S maodien -u 1001 -G maodien

# Create application directory
RUN mkdir -p /opt/maodien/logs && \
    chown -R maodien:maodien /opt/maodien && \
    chmod -R 755 /opt/maodien && \
    chmod -R 775 /opt/maodien/logs

WORKDIR /opt/maodien

# Copy the built jar from builder stage
COPY --from=builder /app/build/libs/*.jar /opt/maodien/maodien.jar

# Change to non-root user
USER maodien

# Expose the port
EXPOSE 8083

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8083/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-Dspring.profiles.active=production", "-jar", "/opt/maodien/maodien.jar"]
