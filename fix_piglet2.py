import sys, re
f = 'src/main/java/com/hainam/worksphere/pigletherd/service/PigletHerdService.java'
with open(f, 'r', encoding='utf-8') as fh: c = fh.read()

c = re.sub(r'\s*\.herdCode\(request\.getHerdCode\(\)\)', '', c)
c = re.sub(r'\s*\.reproductionCode\(request\.getReproductionCode\(\)\)', '', c)
c = re.sub(r'\s*\.herdCode\(request\.getNewHerdCode\(\)\)', '', c)
c = re.sub(r'\s*if\s*\(request\.getReproductionCode\(\)\s*!=\s*null\)\s*herd\.setReproductionCode\(request\.getReproductionCode\(\)\);', '', c)
c = re.sub(r'\s*if\s*\(pigletHerdRepository\.existsActiveByHerdCode\(request\.getHerdCode\(\)\)\)\s*\{[^}]*\}', '', c)
c = re.sub(r'\s*if\s*\(pigletHerdRepository\.existsActiveByHerdCode\(request\.getNewHerdCode\(\)\)\)\s*\{[^}]*\}', '', c)

with open(f, 'w', encoding='utf-8') as fh: fh.write(c)

