/**
 * Wrapper for convenient work with localStorage.
 * It can store any data type, not just strings
 */
export default class Storage {
  /**
   * @static
   * Wrapper for localStorage.clear()
   * @param {Storage} storage localStorage or sessionStorage
   * @description Clears the entire localStorage
   */
  static clear(storage = localStorage) {
    storage.clear();
  }

  /**
   * @static
   * Wrapper for localStorage.getItem(key)
   * @param {string} key
   * @param {Storage} storage localStorage or sessionStorage
   * @returns {Object} Data by the key
   * @description Read data from localStorage for a specific key
   */
  static get(key: string, storage = localStorage) {
    return JSON.parse(<string>storage.getItem(key));
  }

  /**
   * @static
   * Wrapper for localStorage.setItem(key, value)
   * @param {string} key
   * @param {Object} value
   * @param {Storage} storage localStorage or sessionStorage
   * @description Adds data by key
   */
  static set(key: string, value: any, storage = localStorage) {
    return storage.setItem(key, JSON.stringify(value));
  }

  /**
   * @static
   * Wrapper for localStorage.removeItem(key)
   * @param {string} key
   * @param {Storage} storage localStorage or sessionStorage
   * @description Removes data by key
   */
  static remove(key: string, storage = localStorage) {
    return storage.removeItem(key);
  }
}
