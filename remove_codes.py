import os, re

def process_file(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    new_content = content

    # Domain
    new_content = re.sub(r'\s*@Column[^\n]*name\s*=\s*"[a-zA-Z_]+code"[^\n]*\n\s*private\s+String\s+\w+Code;', '', new_content)
    # DTOs
    new_content = re.sub(r'\s*(?:@[A-Z]\w+(?:\([^)]*\))?\s*)*private\s+String\s+[a-zA-Z]+Code;', '', new_content)
    # Repository
    new_content = re.sub(r'\s*boolean\s+exists(?:Active)?By[A-Z]\w*Code\(\w+\s+\w+Code\);', '', new_content)
    new_content = re.sub(r'\s*Optional<[A-Za-z<>]+>\s+find(?:Active)?By[A-Z]\w*Code\(\w+\s+\w+Code\);', '', new_content)
    # Mappers and Services
    new_content = re.sub(r'\.[a-z]+Code\([^)]*\)', '', new_content)
    new_content = re.sub(r'\.get[A-Z]\w*Code\(\)', '""', new_content)

    if new_content != content:
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(new_content)
        return True
    return False

mods = 0
for r, d, f in os.walk('src'):
    if 'authorization' in r or 'employee' in r: continue
    for file in f:
        if file.endswith('.java'):
            if process_file(os.path.join(r, file)):
                mods += 1
print(f"Modifications in {mods} files.")
