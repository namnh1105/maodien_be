import os, re

def fix(c):
    # Regex designed to only match the string code in Domain and Dto definitions
    # Domain fields
    c = re.sub(r'(?m)^\s*@Column\([^)]*name\s*=\s*"[A-Za-z_]+code"[^)]*\)\s*\n\s*private\s+String\s+[A-Za-z]+Code;\s*\n', '', c)
    # Dto fields + validations
    c = re.sub(r'(?m)^\s*(?:@[A-Z]\w+(?:\([^)]*\))?\s*\n)*\s*private\s+String\s+(?!zip|tax|bar)[a-zA-Z]+Code;\s*\n', '', c)
    # Exists in repo
    c = re.sub(r'(?m)^\s*boolean\s+existsActiveBy[A-Z]\w*Code\(\w+\s+\w+Code\);\s*\n', '', c)
    c = re.sub(r'(?m)^\s*Optional<[A-Za-z<>]+>\s+find(?:Active)?By[A-Z]\w*Code\(\w+\s+\w+Code\);\s*\n', '', c)
    return c

for r, d, f in os.walk('src/main/java'):
    if 'authorization' in r or 'employee' in r or 'shared' in r: continue
    for file in f:
        if file.endswith('.java'):
            p = os.path.join(r, file)
            with open(p, 'r', encoding='utf-8') as fh: c = fh.read()
            nc = fix(c)
            if c != nc:
                with open(p, 'w', encoding='utf-8') as fh: fh.write(nc)
