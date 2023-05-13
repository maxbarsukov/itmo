import LocalStorage from 'utils/LocalStorage';

describe('test LocalStorage', () => {
  it('should be defined', () => {
    expect(LocalStorage).toBeDefined();
  });

  it('should not be null', () => {
    expect(LocalStorage).not.toBeNull();
  });

  it('should be an object', () => {
    expect(LocalStorage).toBeInstanceOf(Object);
  });

  it('should get non-existent values as null', () => {
    expect(LocalStorage.get('key')).toBeNull();
  });

  it('should store strings', () => {
    LocalStorage.set('key', 'value');
    expect(LocalStorage.get('key')).toEqual('value');
    expect(localStorage.getItem('key')).toEqual('"value"');
  });

  it('should store objects', () => {
    LocalStorage.set('key', { obj: 'some value' });
    expect(LocalStorage.get('key').obj).not.toBeNull();
    expect(LocalStorage.get('key').obj).toEqual('some value');
  });

  it('should store null', () => {
    LocalStorage.set('key', null);
    expect(LocalStorage.get('key')).toBeNull();
    expect(localStorage.getItem('key')).toEqual('null');
  });

  it('should update values successfully', () => {
    LocalStorage.set('key', 'value');
    LocalStorage.set('key', 'new value');
    expect(LocalStorage.get('key')).toEqual('new value');
  });

  it('should remove values successfully', () => {
    LocalStorage.set('key1', 'value1');
    LocalStorage.set('key2', 'value2');
    LocalStorage.remove('key1');
    expect(LocalStorage.get('key1')).toBeNull();
    expect(localStorage.getItem('key1')).toBeNull();
    expect(LocalStorage.get('key2')).not.toBeNull();
    expect(localStorage.getItem('key2')).not.toBeNull();
  });

  it('should clear values successfully', () => {
    LocalStorage.set('key1', 'value1');
    LocalStorage.set('key2', 'value2');
    LocalStorage.clear();
    expect(LocalStorage.get('key1')).toBeNull();
    expect(localStorage.getItem('key1')).toBeNull();
    expect(LocalStorage.get('key2')).toBeNull();
    expect(localStorage.getItem('key2')).toBeNull();
  });
});
