import Response from 'interfaces/dto/Response';

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

type response = Response<InfoResponse>;
export default response;
