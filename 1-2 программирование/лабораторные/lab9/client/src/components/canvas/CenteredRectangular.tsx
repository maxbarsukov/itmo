import React from 'react';
import { Rect } from 'react-konva';

export type ProductInternalCanvasProps = {
  x: number;
  y: number;
  width: number;
  height: number;
  fill: string;
  stroke: string;
  onMouseOver?: ((e) => void) | null | undefined;
  onClick?: ((e) => void) | null | undefined;
  onDblClick?: ((e) => void) | null | undefined;
};

const CenteredRectangular = (props: ProductInternalCanvasProps) => {
  const { height, width, x, y, fill, stroke, onClick, onDblClick, onMouseOver } = props;


  return (
    <Rect
      height={height}
      width={width}
      x={x - width / 2}
      y={y - height / 2}
      fill={fill}
      stroke={stroke}
      onClick={onClick}
      onDblClick={onDblClick}
      onMouseOver={onMouseOver}
    />
  );
};


export default CenteredRectangular;
