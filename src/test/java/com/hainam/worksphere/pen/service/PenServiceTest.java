package com.hainam.worksphere.pen.service;

import com.hainam.worksphere.BaseUnitTest;
import com.hainam.worksphere.pen.domain.Pen;
import com.hainam.worksphere.pen.domain.PenStatus;
import com.hainam.worksphere.pen.domain.PenType;
import com.hainam.worksphere.pen.dto.request.CreatePenRequest;
import com.hainam.worksphere.pen.dto.request.UpdatePenRequest;
import com.hainam.worksphere.pen.dto.response.PenResponse;
import com.hainam.worksphere.pen.mapper.PenMapper;
import com.hainam.worksphere.pen.repository.PenRepository;
import com.hainam.worksphere.shared.exception.BusinessRuleViolationException;
import com.hainam.worksphere.shared.exception.PenNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("PenService Tests")
class PenServiceTest extends BaseUnitTest {

    @Mock
    private PenRepository penRepository;

    @Mock
    private PenMapper penMapper;

    @InjectMocks
    private PenService penService;

    private UUID penId;
    private UUID actorId;
    private Pen pen;

    @BeforeEach
    void setUp() {
        penId = UUID.randomUUID();
        actorId = UUID.randomUUID();
        pen = Pen.builder()
                .id(penId)
                .name("Pen A")
                .area(25.0)
                .penType(PenType.GROWING)
                .status(PenStatus.EMPTY)
                .build();
    }

    @Test
    @DisplayName("Should create pen successfully")
    void shouldCreatePenSuccessfully() {
        CreatePenRequest request = CreatePenRequest.builder()
                .name("Pen New")
                .area(30.5)
                .areaId(UUID.randomUUID())
                .penType("finishing")
                .status("")
                .build();

        Pen saved = Pen.builder().id(penId).penType(PenType.FINISHING).status(PenStatus.EMPTY).build();
        PenResponse mapped = PenResponse.builder().id(penId).status("EMPTY").build();

        when(penRepository.save(any(Pen.class))).thenReturn(saved);
        when(penMapper.toResponse(saved)).thenReturn(mapped);

        PenResponse result = penService.create(request, actorId);

        ArgumentCaptor<Pen> captor = ArgumentCaptor.forClass(Pen.class);
        verify(penRepository).save(captor.capture());

        Pen created = captor.getValue();
        assertThat(created.getPenType()).isEqualTo(PenType.FINISHING);
        assertThat(created.getStatus()).isEqualTo(PenStatus.EMPTY);
        assertThat(created.getCreatedBy()).isEqualTo(actorId);
        assertThat(result.getId()).isEqualTo(penId);
    }

    @Test
    @DisplayName("Should throw validation error when pen type is invalid")
    void shouldThrowWhenPenTypeIsInvalid() {
        CreatePenRequest request = CreatePenRequest.builder()
                .name("Pen Invalid")
                .penType("unknown")
                .build();

        assertThatThrownBy(() -> penService.create(request, actorId))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("Invalid pen type");

        verifyNoInteractions(penRepository, penMapper);
    }

    @Test
    @DisplayName("Should get all active pens")
    void shouldGetAllActivePens() {
        when(penRepository.findAllActive()).thenReturn(List.of(pen));
        when(penMapper.toResponse(pen)).thenReturn(PenResponse.builder().id(penId).name("Pen A").build());

        List<PenResponse> result = penService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Pen A");
        verify(penRepository).findAllActive();
    }

    @Test
    @DisplayName("Should throw not found when pen does not exist")
    void shouldThrowWhenPenNotFound() {
        UUID unknownId = UUID.randomUUID();
        when(penRepository.findActiveById(unknownId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> penService.getById(unknownId))
                .isInstanceOf(PenNotFoundException.class)
                .hasMessageContaining("Pen not found with id");

        verifyNoInteractions(penMapper);
    }

    @Test
    @DisplayName("Should update pen successfully")
    void shouldUpdatePenSuccessfully() {
        UpdatePenRequest request = UpdatePenRequest.builder()
                .name("Pen Updated")
                .penType("BREEDING")
                .status("IN_USE")
                .build();

        when(penRepository.findActiveById(penId)).thenReturn(Optional.of(pen));
        when(penRepository.save(any(Pen.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(penMapper.toResponse(any(Pen.class))).thenReturn(PenResponse.builder().id(penId).name("Pen Updated").status("IN_USE").build());

        PenResponse result = penService.update(penId, request, actorId);

        assertThat(result.getName()).isEqualTo("Pen Updated");
        assertThat(pen.getName()).isEqualTo("Pen Updated");
        assertThat(pen.getPenType()).isEqualTo(PenType.BREEDING);
        assertThat(pen.getStatus()).isEqualTo(PenStatus.IN_USE);
        assertThat(pen.getUpdatedBy()).isEqualTo(actorId);
        verify(penRepository).save(pen);
    }

    @Test
    @DisplayName("Should soft delete pen successfully")
    void shouldSoftDeletePenSuccessfully() {
        when(penRepository.findActiveById(penId)).thenReturn(Optional.of(pen));

        penService.delete(penId, actorId);

        assertThat(pen.getIsDeleted()).isTrue();
        assertThat(pen.getDeletedAt()).isNotNull();
        assertThat(pen.getDeletedBy()).isEqualTo(actorId);
        verify(penRepository).save(pen);
    }
}
