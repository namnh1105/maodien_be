import re
f = 'src/test/java/com/hainam/worksphere/contract/service/ContractServiceTest.java'
with open(f, 'r', encoding='utf-8') as fh: c = fh.read()
c = re.sub(r'(?m)^\s*\.contractCode\(.*?\)\s*\n', '', c)
with open(f, 'w', encoding='utf-8') as fh: fh.write(c)
