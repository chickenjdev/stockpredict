/* eslint-disable @typescript-eslint/no-unused-vars */
import type { FC } from 'react';
import React from 'react';

import type { Dispatch } from 'umi';
import { connect } from 'umi';
import NoFoundPage from '@/pages/404';

interface WarehouseProps {
  loading: boolean;
  dispatch: Dispatch<any>;
  warehouse: any;
}

const Warehouse: FC<WarehouseProps> = (props) => {
  return (
    <>
      <div>
        <NoFoundPage />
      </div>
    </>
  );
};

export default connect(
  ({
    warehouse,
    loading,
  }: {
    warehouse: any;
    loading: {
      effects: { [key: string]: boolean };
    };
  }) => ({
    warehouse,
    loading: loading.effects['warehouse/fetchWarehouseList'],
  }),
)(Warehouse);
