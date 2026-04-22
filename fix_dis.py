import re
f = 'src/main/java/com/hainam/worksphere/diseasehistory/service/DiseaseHistoryService.java'
with open(f, 'r', encoding='utf-8') as fh: c = fh.read()
# just remove .historyCode(r.getHistoryCode()) and .diseaseCode(r.getDiseaseCode())
c = re.sub(r'\.historyCode\([^)]*\)', '', c)
c = re.sub(r'\.diseaseCode\([^)]*\)', '', c)
c = re.sub(r'if\s*\([^)]*existsActiveByHistoryCode[^)]*\)\s*throw.*?;', '', c)
c = re.sub(r'if\s*\([^)]*getDiseaseCode[^)]*\)\s*e\.setDiseaseCode[^;]*;', '', c)
with open(f, 'w', encoding='utf-8') as fh: fh.write(c)
