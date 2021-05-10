/* eslint-disable @typescript-eslint/no-shadow */
import { Card, Input } from 'antd';
import type { Dispatch } from 'umi';
import { connect } from 'umi';
import type { FC } from 'react';
import React, { useCallback, useEffect, useState } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import styles from './style.less';
import { Provider } from '@/components';
import type { TopupOld } from './data.d';
import { SERVICE_CATEGORY } from '@/utils/constants';

interface AirtimeProps {
  topup: Partial<TopupOld>;
  dispatch: Dispatch<any>;
}

interface CenterState {
  tabKey?: 'topup' | 'card' | 'topup-old' | 'card-old';
}

const Airtime: FC<AirtimeProps> = (props) => {
  const { dispatch } = props;

  const [tabKey, setTabKey] = useState('topup');

  const tabList = [
    {
      key: 'topup',
      tab: 'TOPUP-DATA',
    },
    {
      key: 'card',
      tab: 'CARD-3G/4G',
    },
  ];

  const renderChildrenByTabKey = (tabKey: CenterState['tabKey']) => {
    if (tabKey === 'topup') {
      return (
        <Provider
          key={0}
          isTopup={true}
          dispatch={dispatch}
          serviceCategory={SERVICE_CATEGORY.TOPUP_DATA}
        />
      );
    }
    if (tabKey === 'card') {
      return (
        <Provider
          key={1}
          isTopup={true}
          dispatch={dispatch}
          serviceCategory={SERVICE_CATEGORY.BUYCARD_DATA}
        />
      );
    }
    return null;
  };

  const handleTabChange = useCallback(
    (key: string) => {
      setTabKey(key);
    },
    [setTabKey],
  );

  useEffect(() => {}, []);

  return (
    <PageContainer tabList={tabList} onTabChange={handleTabChange}>
      <Card className={styles.tabsCard} bordered={false}>
        {renderChildrenByTabKey(tabKey as CenterState['tabKey'])}
      </Card>
    </PageContainer>
  );
};

export default connect(
  ({
    topup,
    loading,
  }: {
    topup: any;
    loading: {
      effects: { [key: string]: boolean };
    };
  }) => ({
    topup,
    loading: loading.effects['topup/getTopupOld'],
  }),
)(Airtime);
