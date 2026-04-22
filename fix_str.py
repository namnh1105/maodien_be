import sys
f = 'src/main/java/com/hainam/worksphere/diseasehistory/service/DiseaseHistoryService.java'
with open(f, 'r', encoding='utf-8') as fh: c = fh.read()
c = c.replace('.historyCode(r.getHistoryCode())', '')
c = c.replace('.diseaseCode(r.getDiseaseCode())', '')
c = c.replace('if (r.getDiseaseCode() != null) e.setDiseaseCode(r.getDiseaseCode());', '')
c = c.replace('if (repo.existsActiveByHistoryCode(r.getHistoryCode())) throw new BusinessRuleViolationException("Disease history code already exists: " + r.getHistoryCode());', '')
with open(f, 'w', encoding='utf-8') as fh: fh.write(c)
