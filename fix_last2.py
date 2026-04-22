import os, re
f = 'src/main/java/com/hainam/worksphere/diseasehistory/service/DiseaseHistoryService.java'
with open(f, 'r', encoding='utf-8') as fh: c = fh.read()
c = re.sub(r'\.historyCode\(null\)', '', c)
with open(f, 'w', encoding='utf-8') as fh: fh.write(c)
