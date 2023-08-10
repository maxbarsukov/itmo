type InfoResponse = {
  productsCount: number;
  databaseName: string;
  databaseVersion: string;
  driverName: string;
  driverVersion: string;
  JDBCMajorVersion: number;
  JDBCMinorVersion: number;
  maxConnections: number;
};

export default InfoResponse;
