/**
 * Wrapper for convenient work with localStorage.
 * It can store any data type, not just strings
 */
export default class LocalStorage {
  /**
   * @static
   * Wrapper for localStorage.clear()
   * @description Clears the entire localStorage
   */
  static clear() {
    localStorage.clear();
  }

  /**
   * @static
   * Wrapper for localStorage.getItem(key)
   * @param {string} key
   * @returns {Object} Data by the key
   * @description Read data from localStorage for a specific key
   */
  static get(key: string) {
    return JSON.parse(<string>localStorage.getItem(key));
  }

  /**
   * @static
   * Wrapper for localStorage.setItem(key, value)
   * @param {string} key
   * @param {Object} value
   * @description Adds data by key
   */
  static set(key: string, value: object | string) {
    return localStorage.setItem(key, JSON.stringify(value));
  }

  /**
   * @static
   * Wrapper for localStorage.removeItem(key)
   * @param {string} key
   * @description Removes data by key
   */
  static remove(key: string) {
    return localStorage.removeItem(key);
  }
}
