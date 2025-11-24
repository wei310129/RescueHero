// 建立 rescuehero 資料庫的使用者
db = db.getSiblingDB('rescuehero');

db.createUser({
  user: 'hero',
  pwd: 'rescue',
  roles: [
    {
      role: 'readWrite',
      db: 'rescuehero'
    }
  ],
  mechanisms: ['SCRAM-SHA-256', 'SCRAM-SHA-1']
});

print('User hero created successfully in rescuehero database');

