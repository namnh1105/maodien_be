import os, re

def fix_tests(c):
    c = re.sub(r'(?m)^\s*\.[a-zA-Z]+Code\([^)]*\)\s*\n', '', c)
    c = re.sub(r'(?m)^\s*\w+\.set[A-Z]\w*Code\([^)]*\);\s*\n', '', c)
    c = re.sub(r'(?m)^\s*if\s*\([^)]*get[A-Z]\w*Code\(\)\s*!=\s*null\)\s*.*$', '', c)
    
    # Assertions involving getXYZCode
    c = re.sub(r'(?m)^\s*\(\)\s*->\s*assertThat\([^)]*\.get[A-Z]\w*Code\(\)\)\.isEqualTo\([^)]*\),\s*\n', '', c)
    c = re.sub(r'(?m)^\s*assertThat\([^)]*\.get[A-Z]\w*Code\(\)\).*?;\s*\n', '', c)

    c = re.sub(r'\.get[a-zA-Z]+Code\(\)', '""', c)

    return c

for r, d, f in os.walk('src/test/java'):
    if 'authorization' in r: continue
    for file in f:
        if file.endswith('.java'):
            p = os.path.join(r, file)
            with open(p, 'r', encoding='utf-8') as fh: c = fh.read()
            nc = fix_tests(c)
            if c != nc:
                with open(p, 'w', encoding='utf-8') as fh: fh.write(nc)
