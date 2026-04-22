import sys
f = 'src/main/java/com/hainam/worksphere/pigletherd/service/PigletHerdService.java'
with open(f, 'r', encoding='utf-8') as fh: c = fh.read()
c = c.replace('.herdCode(request.getHerdCode())', '')
c = c.replace('.reproductionCode(request.getReproductionCode())', '')
c = c.replace('.herdCode(request.getNewHerdCode())', '')
c = c.replace('if (request.getReproductionCode() != null) herd.setReproductionCode(request.getReproductionCode());', '')
c = c.replace('if (pigletHerdRepository.existsActiveByHerdCode(request.getHerdCode())) {\n            throw new BusinessRuleViolationException("Herd code already exists: " + request.getHerdCode());\n        }', '')
c = c.replace('if (pigletHerdRepository.existsActiveByHerdCode(request.getNewHerdCode())) {\n            throw new BusinessRuleViolationException("Herd code already exists: " + request.getNewHerdCode());\n        }', '')
with open(f, 'w', encoding='utf-8') as fh: fh.write(c)
