import React, { createRef, useEffect, useRef, useState } from 'react';
import Product from 'interfaces/models/Product';
import { makeStyles } from '@material-ui/core/styles';
import { useSelector } from 'hooks';
import { useContainerDimensions} from 'hooks/useContainerDimensions';
import { Layer, Line, Stage } from 'react-konva';

import { useSnackbar } from 'notistack';
import { useTranslation } from 'react-i18next';

import ProductCanvas from 'components/canvas/ProductCanvas';
import AddOrEditMenu from 'components/blocks/Table/modals/AddOrEdit';
import DeleteMenu from 'components/blocks/Table/modals/Delete';
import ProductInfo from '../../components/blocks/ProductInfo';

const useStyles = makeStyles(theme => ({
  root: {
    background: theme.palette.background.default,
    padding: 0,
    width: '100%',
    height: '100%',
    justifyContent: 'center',
    margin: 'auto',
  },
  stage: {
    border: '3px solid black',
    width: '100%',
    height: '100%',
    margin: 'auto',
  },
}));

const convertVhToPx = vh => {
  const oneVhInPx = window.innerHeight / 100;
  return oneVhInPx * vh;
};

const Visualizer = ({ data }: { data: Product[] }) => {
  const classes = useStyles();
  const { enqueueSnackbar } = useSnackbar();
  const { t } = useTranslation();

  const ref = useRef<HTMLCanvasElement>(null);
  const container = useRef<HTMLDivElement>(null);
  const [dimensions, setDimensions] = useState({ width: 0, height: 0 });

  const width = useContainerDimensions(container).width - 4;
  const height = convertVhToPx(70);

  const [isEditOpen, setEditOpen] = useState<boolean>(false);
  const [isDeleteOpen, setDeleteOpen] = useState<boolean>(false);
  const [isProductInfoOpen, setProductInfoOpen] = useState<boolean>(false);
  const [currentId, setCurrentId] = useState<number>();

  const openEditMenu = () => setEditOpen(true);
  const openDeleteMenu = () => setDeleteOpen(true);
  const openProductInfo = () => setProductInfoOpen(true);

  const userData = useSelector(state => state.auth.user);
  const isLoggedIn = useSelector(store => store.auth.isLoggedIn);

  useEffect(() => {
    const canvas = ref.current;
    if (canvas == null) return;

    const context = canvas.getContext('2d');
    data.forEach(product => {
      context.fillRect(product.x, product.y, 10, 10);
    });
  }, [data]);

  const rangeX = 2 * Math.abs(data.reduce((prev, cur) => {
    if (Math.abs(prev.x) < Math.abs(cur.x)) {
      return cur;
    }
    return prev;
  }).x) * 1.25;

  const rangeY = 2 * Math.abs(data.reduce((prev, cur) => {
    if (Math.abs(prev.y) < Math.abs(cur.y)) {
      return cur;
    }
    return prev;
  }).y) * 1.25;

  const onDelete = (product) => {
    if (!isLoggedIn) {
      enqueueSnackbar(t('pages.Home.Table.errors.notLoggedIn'), {
        variant: 'error', autoHideDuration: 4000,
      });
      return;
    }
    if (product.creator.id !== userData?.id) {
      enqueueSnackbar(t('pages.Home.Table.errors.badOwner'), {
        variant: 'error', autoHideDuration: 2000,
      });
      return;
    }
    setCurrentId(product.id);
    openDeleteMenu();
  };

  const onUpdate = (product) => {
    if (!isLoggedIn) {
      enqueueSnackbar(t('pages.Home.Table.errors.notLoggedIn'), {
        variant: 'error', autoHideDuration: 4000,
      });
      return;
    }
    if (product.creator.id !== userData?.id) {
      enqueueSnackbar(t('pages.Home.Table.errors.badOwner'), {
        variant: 'error', autoHideDuration: 2000,
      });
      return;
    }
    setCurrentId(product.id);
    openEditMenu();
  };

  return (
    <div className={classes.root}>
      <div className={classes.stage} ref={container}>
        <Stage
          width={width}
          height={height}
        >
          <Layer>
            <Line
              x={0}
              y={height / 2}
              points={[0, 0, width, 0]}
              tension={0.7}
              stroke="black"
            />
            <Line
              x={width / 2}
              y={0}
              points={[0, 0, 0, height]}
              tension={0.7}
              stroke="black"
            />
            {data.map(product => (
              <ProductCanvas
                key={product.id}
                product={product}
                canvasWidth={width}
                canvasHeight={height}
                rangeX={rangeX}
                rangeY={rangeY}
                onMouseOver={event => {
                  setDimensions(
                    {
                      height: event.evt.clientY,
                      width: event.evt.clientX,
                    }
                  );
                  setCurrentId(product.id);
                  openProductInfo();
                }}
                onClick={() => onUpdate(product)}
                onDblClick={() => onDelete(product)}
               />
              ))}
          </Layer>
        </Stage>
        {isEditOpen &&
          <AddOrEditMenu productId={currentId} isOpen={isEditOpen} setOpen={setEditOpen} />
        }
        {isDeleteOpen &&
          <DeleteMenu productId={currentId} isOpen={isDeleteOpen} setOpen={setDeleteOpen} />
        }
        {isProductInfoOpen &&
          <ProductInfo
            productId={currentId}
            x={dimensions.width}
            y={dimensions.height}
            handleDelete={onDelete}
            handleUpdate={onUpdate}
            isOpen={isProductInfoOpen}
            setOpen={setProductInfoOpen}
          />
        }
      </div>
    </div>
  );
};

export default Visualizer;
