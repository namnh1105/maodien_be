import os, re

def strip_service_remnants(c):
    c = re.sub(r'(?m)^\s*\.historyCode\(null\)', '', c)
    c = re.sub(r'(?m)^\s*saved\.setPenCode.*\n', '', c)
    c = re.sub(r'(?m)^\s*saved\.setPigCode.*\n', '', c)
    return c

for p in [
    'src/main/java/com/hainam/worksphere/diseasehistory/service/DiseaseHistoryService.java',
    'src/main/java/com/hainam/worksphere/pen/service/PenService.java',
    'src/main/java/com/hainam/worksphere/pig/service/PigService.java'
]:
    with open(p, 'r', encoding='utf-8') as fh: c = fh.read()
    nc = strip_service_remnants(c)
    if c != nc:
        with open(p, 'w', encoding='utf-8') as fh: fh.write(nc)
