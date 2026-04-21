import json, sys
path = '/workspace/src/validation.py'
edits = json.load(open('/tmp/_edits_029f23cc.json'))
try:
    content = open(path).read()
except FileNotFoundError:
    print(f'WARN: {path} not found, skipping edits')
    sys.exit(0)
for e in edits:
    old, new = e.get('old',''), e.get('new','')
    if old and old in content:
        content = content.replace(old, new, 1)
        print(f'Applied edit in {path}')
    else:
        print(f'WARN: edit not matched: {old[:60]}')
open(path, 'w').write(content)
print(f'Modified: {path}')
