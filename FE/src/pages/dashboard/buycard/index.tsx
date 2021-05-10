/* eslint-disable @typescript-eslint/no-shadow */
/* eslint-disable no-restricted-syntax */
/* eslint-disable @typescript-eslint/no-unused-vars */
import React, { useLayoutEffect, useState, useCallback, useEffect, useRef } from 'react';
import type { Dispatch } from 'umi';
import { connect } from 'umi';
import { List, Spin, Divider, Card, Affix, Button } from 'antd';
import { Chart, Interval } from 'bizcharts';
import { GROUP_BALANCING_BUYCARD } from '@/utils/constants';

const ItemAnalysis = ({ data }: any) => {
  const { store, dataChart } = (data as any) || {};
  return (
    <div>
      <List.Item>
        <Card bodyStyle={{ margin: 0, padding: 0 }}>
          <h4 style={{ margin: 20 }}>{store}</h4>
          <Chart height={300} autoFit data={dataChart} appendPadding={[20, 0]}>
            <Interval
              position="amount*count"
              size={20}
              label={[
                'count',
                (val) => {
                  return {
                    content: val,
                    style: {
                      fill: 'red',
                      fontSize: 14,
                      fontWeight: 'bold',
                    },
                  };
                },
              ]}
            />
          </Chart>
        </Card>
        <Divider plain>***</Divider>
      </List.Item>
    </div>
  );
};

interface AnalysisProps {
  dashboardBuycard: any;
  dispatch: Dispatch<any>;
  loading: boolean;
}

const Analysis: React.FC<AnalysisProps> = (props) => {
  const { dispatch, dashboardBuycard } = props;
  const reqRef = useRef<number>(0);
  const [isShowSpin, setLoading] = useState(true);
  const [dataAnalysis, setAnalysis] = useState(new Array());

  const useDataAnalysis = useCallback(
    (dashboard) => {
      const { result } = dashboard || {};
      const { data } = result || {};
      const { listBalancing = [] } = data || {};
      const listFilterBalancing = listBalancing.filter((item: any) => {
        const { store } = item || {};
        return GROUP_BALANCING_BUYCARD.includes(store);
      });
      const arrData = [] as any;
      listFilterBalancing.forEach((element: any) => {
        const { store, data = {} } = (element as any) || {};
        const dataChart = [] as any;
        for (const [key, value] of Object.entries(data)) {
          dataChart.push({ amount: key, count: Number(Math.max(Number(value), 0)) });
        }
        dataChart.sort((item1: any, item2: any) => {
          return Number(item1.amount) - Number(item2.amount);
        });
        arrData.push({ store, dataChart });
      });
      setAnalysis(arrData);
      setLoading(false);
    },
    [setLoading, setAnalysis],
  );

  useEffect(() => {
    if (dispatch) {
      reqRef.current = requestAnimationFrame(() => {
        dispatch({
          type: 'dashboardBuycard/getBalancing',
          params: {},
        });
      });
    }
    return () => {};
  }, [dispatch]);

  useLayoutEffect(() => {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    useDataAnalysis(dashboardBuycard);

    return () => {};
  }, [dashboardBuycard, useDataAnalysis]);
  return (
    <div>
      <Spin spinning={isShowSpin}>
        <List
          grid={{ gutter: 10, column: 2 }}
          dataSource={dataAnalysis}
          renderItem={(item: any) => <ItemAnalysis data={item} />}
        />
      </Spin>
    </div>
  );
};

export default connect(
  ({
    dashboardBuycard,
    loading,
  }: {
    dashboardBuycard: any;
    loading: {
      effects: { [key: string]: boolean };
    };
  }) => ({
    dashboardBuycard,
    loading: loading.effects['dashboardBuycard/getBalancing'],
  }),
)(Analysis);
