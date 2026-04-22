import os, re

def strip_service(c):
    # Remove lines wrapping existsActiveBy...
    c = re.sub(r'(?m)^\s*if\s*\([^)]*existsActiveBy[A-Z]\w*Code[^)]*\)\s*\{\s*\n(?:.*\n)*?\s*\}\s*\n', '', c)
    # also one-liners like if (...) throw ... ;
    c = re.sub(r'(?m)^\s*if\s*\([^)]*existsActiveBy[A-Z]\w*Code[^)]*\)\s*throw.*?\n', '', c)
    # builder lines
    c = re.sub(r'(?m)^\s*\.[a-z]+Code\([^)]*\)\s*\n', '', c)
    # set code
    c = re.sub(r'(?m)^\s*\w+\.set[a-zA-Z]+Code\([^)]*\);\s*\n', '', c)
    # if (request.getxxxCode != null) conditions
    c = re.sub(r'(?m)^\s*if\s*\([^)]*get[A-Z]\w*Code\(\)\s*!=\s*null\)\s*(?:\{\s*\n(?:.*\n)*?\s*\})?(?:[^{]*;\s*\n)?', '', c)
    # mother.getPigCode() etc. replacing with "" if any
    c = re.sub(r'\w+\.get[A-Z]\w*Code\(\)', 'null', c)
    return c

for r, d, f in os.walk('src/main/java'):
    if 'authorization' in r or 'employee' in r or 'shared' in r: continue
    for file in f:
        if file.endswith('.java'):
            p = os.path.join(r, file)
            with open(p, 'r', encoding='utf-8') as fh: c = fh.read()
            nc = strip_service(c)
            if c != nc:
                with open(p, 'w', encoding='utf-8') as fh: fh.write(nc)
