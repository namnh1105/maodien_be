import os, re

def fix(c):
    # mappers
    c = re.sub(r'(?m)^\s*@Mapping\([^)]*Code[^)]*\)\s*\n', '', c)
    # services builders
    c = re.sub(r'(?m)^\s*\.[a-zA-Z]+Code\([^)]*\)\s*\n', '', c)
    # services sets
    c = re.sub(r'(?m)^\s*\w+\.set[A-Z]\w*Code\([^)]*\);\s*\n', '', c)
    # .get...Code occurrences
    # c = re.sub(r'\.get[A-Z]\w*Code\(\)', '""', c) # Maybe too risky? Let's just do it
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
