import os, re
import sys

def fix_test_file(content):
    # Removing statements completely
    c = re.sub(r'(?m)^\s*\.[a-zA-Z]+Code\([^)]*\)\s*\n', '', content)
    c = re.sub(r'(?m)^\s*\w+\.set[a-zA-Z]+Code\([^)]*\);\s*\n', '', c)
    c = re.sub(r'(?m)^\s*\(\)\s*->\s*assertThat\([^)]*\.get[a-zA-Z]+Code\(\)\)[^;]*,\s*\n', '', c)
    c = re.sub(r'(?m)^\s*\(\)\s*->\s*assertThat\([^)]*\)[^;]*\.get[a-zA-Z]+Code\(\)[^;]*,\s*\n', '', c)
    c = re.sub(r'(?m)^\s*assertThat\([^)]*\.get[a-zA-Z]+Code\(\)\)[^;]*;\s*\n', '', c)
    
    # Specific employee code stuff
    c = re.sub(r'(?m)^\s*when\([^)]*existsActiveByEmployeeCode[^)]*\)\.thenReturn[^;]*;\s*\n', '', c)
    c = re.sub(r'(?m)^\s*verify\([^)]*\)\.existsActiveByEmployeeCode[^;]*;\s*\n', '', c)
    c = re.sub(r'(?m)^\s*\(\)\s*->\s*verify\([^)]*\)\.existsActiveByEmployeeCode[^;]*,\s*\n', '', c)
    return c

for r, d, f in os.walk('src/test/java/com/hainam/worksphere'):
    if 'authorization' in r: continue
    for file in f:
        if file.endswith('.java'):
            filepath = os.path.join(r, file)
            with open(filepath, 'r', encoding='utf-8') as fh: c = fh.read()
            new_c = fix_test_file(c)
            # handle 'request.getEmployeeCode()' etc. leftovers inside other strings
            new_c = re.sub(r'request\.get[a-zA-Z]+Code\(\)', '"xxx"', new_c)
            new_c = re.sub(r'testEmployee\.getEmployeeCode\(\)', '"xxx"', new_c)
            if c != new_c:
                with open(filepath, 'w', encoding='utf-8') as fh: fh.write(new_c)

