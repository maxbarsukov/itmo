import React from 'react';
import {Circle, Ring, Wedge} from 'react-konva';
import CenteredRectangular, {
  ProductInternalCanvasProps,
} from 'components/canvas/CenteredRectangular';

import Product from 'interfaces/models/Product';
import Konva from 'konva';

export type ProductCanvasProps = {
  product: Product;
  canvasWidth: number;
  canvasHeight: number;
  rangeX: number;
  rangeY: number;
  onClick: (e?: Konva.KonvaEventObject<MouseEvent>) => void;
  onDblClick: (e?: Konva.KonvaEventObject<MouseEvent>) => void;
  onMouseOver: (e?: Konva.KonvaEventObject<MouseEvent>) => void;
};

const ProductCanvas = ({
  product,
  canvasWidth: width,
  canvasHeight: height,
  rangeX,
  rangeY,
  onMouseOver,
  onClick,
  onDblClick,
}: ProductCanvasProps) => {
  const transformedX = product.x / rangeX * width + width / 2;
  const transformedY = -product.y / rangeY * height + height / 2;

  const colorGenerator = (id: number): string => {
    const r = (id * 107 % 238 + 17).toString(16);
    const g = (id * 163 % 238 + 17).toString(16);
    const b = (id * 89 % 238 + 17).toString(16);
    return `#${r}${g}${b}`;
  };

  const size = 50;

  const props: ProductInternalCanvasProps = {
    width: size,
    height: size,
    x: transformedX,
    y: transformedY,
    fill: colorGenerator(product.creator.id),
    onClick: e => onClick(e),
    onMouseOver: e => onMouseOver(e),
    onDblClick: e => onDblClick(e),
    stroke: 'black',
  };

  if (product.unitOfMeasure == 'KILOGRAMS') {
    return <Circle {...props} />;
  }
  if(product.unitOfMeasure == 'LITERS') {
    return <CenteredRectangular {...props}/>;
  }
  if(product.unitOfMeasure == 'SQUARE_METERS') {
    return <Ring innerRadius={10} outerRadius={size} {...props}/>;
  }
  return (
    <Wedge
      angle={product.price % 180 + 45}
      radius={size}
      {...props}
    />
  );
};

export default React.memo(ProductCanvas);
