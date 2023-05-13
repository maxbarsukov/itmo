import Storage from 'utils/Storage';

describe('test Storage', () => {
  it('should be defined', () => {
    expect(Storage).toBeDefined();
  });

  it('should not be null', () => {
    expect(Storage).not.toBeNull();
  });

  it('should be an object', () => {
    expect(Storage).toBeInstanceOf(Object);
  });

  it('should get non-existent values as null', () => {
    expect(Storage.get('key')).toBeNull();
  });

  it('should store strings', () => {
    Storage.set('key', 'value');
    expect(Storage.get('key')).toEqual('value');
    expect(localStorage.getItem('key')).toEqual('"value"');
  });

  it('should store objects', () => {
    Storage.set('key', { obj: 'some value' });
    expect(Storage.get('key').obj).not.toBeNull();
    expect(Storage.get('key').obj).toEqual('some value');
  });

  it('should store null', () => {
    Storage.set('key', null);
    expect(Storage.get('key')).toBeNull();
    expect(localStorage.getItem('key')).toEqual('null');
  });

  it('should update values successfully', () => {
    Storage.set('key', 'value');
    Storage.set('key', 'new value');
    expect(Storage.get('key')).toEqual('new value');
  });

  it('should remove values successfully', () => {
    Storage.set('key1', 'value1');
    Storage.set('key2', 'value2');
    Storage.remove('key1');
    expect(Storage.get('key1')).toBeNull();
    expect(localStorage.getItem('key1')).toBeNull();
    expect(Storage.get('key2')).not.toBeNull();
    expect(localStorage.getItem('key2')).not.toBeNull();
  });

  it('should clear values successfully', () => {
    Storage.set('key1', 'value1');
    Storage.set('key2', 'value2');
    Storage.clear();
    expect(Storage.get('key1')).toBeNull();
    expect(localStorage.getItem('key1')).toBeNull();
    expect(Storage.get('key2')).toBeNull();
    expect(localStorage.getItem('key2')).toBeNull();
  });
});
