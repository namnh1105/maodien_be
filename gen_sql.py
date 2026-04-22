import os, re
import subprocess

diff_out = subprocess.check_output('git diff src/main/java/com/hainam/worksphere/*/domain/*.java'.split(), encoding='utf-8')
lines = diff_out.splitlines()

tables_cols = []
current_file = ''
for line in lines:
    if line.startswith('+++ b/'):
        current_file = line[6:]
    elif line.startswith('-    @Column(name = "'):
        col_name = re.search(r'name = "([^"]+)"', line).group(1)
        # read original file to find table name
        with open(current_file, 'r', encoding='utf-8') as f:
            c = f.read()
            tb = re.search(r'@Table\s*\(\s*name\s*=\s*"([^"]+)"\s*\)', c)
            if tb:
                tables_cols.append((tb.group(1), col_name))
            else:
                print('No table for ' + current_file)

with open('database-migration-2026-04-21-remove-pen-code.sql', 'a', encoding='utf-8') as f:
    for tb, col in list(set(tables_cols)):
        f.write(f"\nALTER TABLE {tb} DROP CONSTRAINT IF EXISTS uq_{tb}_{col};\n")
        f.write(f"DROP INDEX IF EXISTS idx_{tb}_{col};\n")
        f.write(f"ALTER TABLE {tb} DROP COLUMN IF EXISTS {col};\n")
print(f"Appended {len(tables_cols)} columns")
