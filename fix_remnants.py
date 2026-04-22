import os, re

def strip_service_remnants(c):
    c = re.sub(r'(?m)^\s*\.[a-zA-Z]+Code\(null\)\s*\n', '', c)
    c = re.sub(r'(?m)^\s*\.reproductionCode\(null\)\s*\n', '', c)
    c = re.sub(r'(?m)^\s*saved\.set[A-Z]\w*Code\([^)]*\)\);\s*\n', '', c)
    c = re.sub(r'\.diseaseCode\(null\)', '', c)
    c = re.sub(r'(?m)^\s*DiseaseHistory\s+e\s*=\s*DiseaseHistory\.builder\(\)\.historyCode\(null\)\.pigId\(r\.getPigId\(\)\)\.diseaseCode\(null\)', 'DiseaseHistory e = DiseaseHistory.builder().pigId(r.getPigId())', c)
    return c

for r, d, f in os.walk('src/main/java'):
    if 'authorization' in r or 'employee' in r or 'shared' in r: continue
    for file in f:
        if file.endswith('.java'):
            p = os.path.join(r, file)
            with open(p, 'r', encoding='utf-8') as fh: c = fh.read()
            nc = strip_service_remnants(c)
            if c != nc:
                with open(p, 'w', encoding='utf-8') as fh: fh.write(nc)
