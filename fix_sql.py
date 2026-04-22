import re
with open('database-migration-2026-04-21-remove-pen-code.sql', 'r', encoding='utf-8') as f:
    c = f.read()
c = c.replace('\nCOMMIT;', '')
c += '\nCOMMIT;\n'
with open('database-migration-2026-04-21-remove-pen-code.sql', 'w', encoding='utf-8') as f:
    f.write(c)
