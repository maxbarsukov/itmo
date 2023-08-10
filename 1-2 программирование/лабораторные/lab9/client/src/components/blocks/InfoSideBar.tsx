import React, { useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import purple from '@material-ui/core/colors/purple';
import { Skeleton } from '@material-ui/lab';
import { Divider } from '@material-ui/core';
import {
  CategoryRounded as Category,
  SdStorageRounded as SdStorage,
  SdStorageOutlined,
  DriveEtaRounded as DriveEta,
  DriveEtaOutlined,
  HdrStrongRounded as HdrStrong,
  HdrWeakRounded as HdrWeak,
  SettingsInputHdmiOutlined,
  UpdateRounded as Update,
  AccessibilityRounded as Accessibility,
} from '@material-ui/icons';

import SideBlock from 'components/blocks/SideBlock';
import Sidebar from 'components/blocks/Sidebar';

import { getInfo } from 'store/info/actions';
import { useDispatch, useSelector } from 'hooks';
import { useTranslation } from 'react-i18next';
import InfoResponse from 'interfaces/dto/InfoResponse';
import dayjs from 'dayjs';

const useSkeletonStyles = makeStyles((theme) => ({
  skeleton: {
    backgroundColor: theme.palette.action.hover,
    borderRadius: 8,
  },
}));

const useStyles = makeStyles((theme) => ({
  infoChildrenContainerProps: {
    marginBottom: 12,
    display: 'flex',
    flexDirection: 'column',
    padding: theme.spacing(0, 2),
  },
}));

const useInfoItemStyles = makeStyles((theme) => ({
  root: {
    display: 'flex',
    alignItems: 'center',
    textDecoration: 'none !important',
    background: 'transparent !important',
    marginTop: theme.spacing(2),
    marginBottom: theme.spacing(1),
    color: theme.palette.text.primary,
    '&:first-child': {
      marginTop: 0,
    },
  },
  icon: {
    color: theme.palette.primary.main,
    marginRight: theme.spacing(1.5),
    width: 36,
    height: 36,
    borderRadius: 4,
  },
  title: {
    fontSize: 13,
    fontWeight: 700,
    lineHeight: '18px',
  },
  value: {
    textAlign: 'right',
    marginLeft: 'auto',
    fontSize: 13,
    fontWeight: 700,
    lineHeight: '18px',
    paddingRight: theme.spacing(0.1),
    color: theme.palette.type === 'dark' ? purple[200] : purple.A700,
  },
}));

const icons = {
  Category,
  SdStorage,
  SdStorageOutlined,
  DriveEta,
  DriveEtaOutlined,
  HdrStrong,
  HdrWeak,
  SettingsInputHdmiOutlined,
  Accessibility,
  Update,
};

type InfoAlias = keyof InfoResponse | 'lastUpdated' | 'updatedBy';
interface InfoCard {
  alias: InfoAlias;
  title: string;
  value?: string | number;
  icon: keyof typeof icons;
}

const InfoItem: React.FC<{ data: InfoCard }> = ({ data }) => {
  const classes = useInfoItemStyles();
  const Icon = icons[data.icon];

  return (
    <div className={classes.root}>
      <Icon className={classes.icon}/>
      <span className={classes.title}>{data.title}</span>
      <span className={classes.value}>{data.value}</span>
    </div>
  );
};

const InfoSideBlockSkeleton = () => {
  const classes = useSkeletonStyles();
  const infoItemClasses = useInfoItemStyles();
  return (
    <>
      {[...Array(8)].map((_, i) => (
        <div key={i} className={infoItemClasses.root}>
          <Skeleton
            variant="rect"
            className={[classes.skeleton, infoItemClasses.icon].join(' ')}
          />
          <Skeleton
            variant="rect"
            width={84}
            height={14}
            className={[classes.skeleton, infoItemClasses.title].join(' ')}
          />
          <Skeleton
            variant="rect"
            width={44}
            height={14}
            className={[classes.skeleton, infoItemClasses.value].join(' ')}
          />
        </div>
      ))}
    </>
  );
};

const InfoSidebar = () => {
  const dispatch = useDispatch();
  const classes = useStyles();
  const { t } = useTranslation();

  const isFetched = useSelector(state => state.info.fetched);
  const isFetching = useSelector(state => state.info.fetching);
  const fetchError = useSelector(state => state.info.error);
  const info = useSelector(state => state.info.info);
  const productsCount = useSelector(state => state.products.productsCount);
  const lastUpdated = useSelector(state => state.products.lastUpdated);
  const updatedBy = useSelector(state => state.products.updatedBy);

  useEffect(() => {
    dispatch(getInfo());
    if (fetchError) {
      console.error('Fetch error in InfoSidebar (fetching info):', fetchError);
    }
  }, []);

  const infoAliasToIcon: { [key in Partial<InfoAlias>]: keyof typeof icons} = {
    productsCount: 'Category',
    databaseName: 'SdStorage',
    databaseVersion: 'SdStorageOutlined',
    driverName: 'DriveEta',
    driverVersion: 'DriveEtaOutlined',
    JDBCMajorVersion: 'HdrStrong',
    JDBCMinorVersion: 'HdrWeak',
    maxConnections: 'SettingsInputHdmiOutlined',
    lastUpdated: 'Update',
    updatedBy: 'Accessibility',
  };

  const getInfoValue = (alias: string) => {
    const ts = dayjs(lastUpdated).calendar();
    switch (alias) {
      case 'productsCount': return productsCount;
      case 'lastUpdated': return ts;
      case 'updatedBy': return updatedBy;
      default: return (info ? info[alias] : null);
    }
  };

  const infoItems: InfoCard[] = Object.keys(infoAliasToIcon).map(alias => ({
    alias: alias as InfoAlias,
    title: t(`pages.Home.Sidebar.Info.${alias}`),
    value: getInfoValue(alias),
    icon: infoAliasToIcon[alias],
  }));

  return (
    <Sidebar>
      {!fetchError && (
        <SideBlock
          childrenContainerProps={{
            className: classes.infoChildrenContainerProps,
          }}
          title={t`pages.Home.Sidebar.Info.Information`}
        >
          {isFetching && (
            <InfoSideBlockSkeleton />
          )}
          {isFetched &&
            infoItems.map((e, i) => (
              <>
                <InfoItem data={e} key={e.alias} />
                {infoItems.length - 1 !== i && <Divider />}
              </>
            ))}
        </SideBlock>
      )}
    </Sidebar>
  );
};

export default InfoSidebar;
