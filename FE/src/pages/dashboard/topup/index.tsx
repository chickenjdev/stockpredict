/* eslint-disable @typescript-eslint/no-shadow */
/* eslint-disable no-restricted-syntax */
/* eslint-disable @typescript-eslint/no-unused-vars */
import React, { useLayoutEffect, useState, useCallback, useEffect, useRef } from 'react';
import type { Dispatch } from 'umi';
import { connect } from 'umi';
import { List, Spin, Divider, Card, Affix, Button } from 'antd';
import {
  Chart,
  Interval,
  G2,
  Geom,
  Axis,
  Tooltip,
  Coord,
  Label,
  Legend,
  View,
  Guide,
  Shape,
  Facet,
  Util,
} from 'bizcharts';
import { GROUP_BALANCING_TOPUP } from '@/utils/constants';

function getComponent() {
  var data = [];
  let chart;
  const scale = {
    time: {
      alias: '时间',
      type: 'time',
      mask: 'MM:ss',
      tickCount: 10,
      nice: false,
    },
    temperature: {
      alias: '平均温度(°C)',
      min: 10,
      max: 35,
    },
    type: {
      type: 'cat',
    },
  };

  class SliderChart extends React.Component {
    constructor() {
      super();
      this.state = {
        data,
      };
    }

    componentDidMount() {
      setInterval(() => {
        var now = new Date();
        var time = now.getTime();
        var temperature1 = ~~(Math.random() * 5) + 22;
        var temperature2 = ~~(Math.random() * 7) + 17;

        if (data.length >= 200) {
          data.shift();
          data.shift();
        }

        data.push({
          time: time,
          temperature: temperature1,
          type: '记录1',
        });
        data.push({
          time: time,
          temperature: temperature2,
          type: '记录2',
        });
        this.setState({
          data,
        });
      }, 1000);
    }
    render() {
      console.log(data.length);

      return (
        <List.Item>
          <Card bodyStyle={{ margin: 0, padding: 0 }}>
            <Chart
              height={300}
              autoFit
              data={data}
              appendPadding={[20, 0]}
              scale={scale}
              onGetG2Instance={(g2Chart: any) => {
                chart = g2Chart;
              }}
            >
              <Tooltip />
              {data.length !== 0 ? <Axis /> : ''}
              <Legend />
              <Geom
                type="line"
                position="time*temperature"
                color={['type', ['#ff7f0e', '#2ca02c']]}
                shape="smooth"
                size={1}
              />
            </Chart>
          </Card>
        </List.Item>
      );
    }
  }

  return SliderChart;
}
const Linerealtime = () => {
  const SliderChart = getComponent();
  return (
    <div>
      <SliderChart />
    </div>
  );
};

const ItemAnalysis = ({ data }: any) => {
  const { store, dataChart } = (data as any) || {};
  const [topupChart, setTopupChart] = useState<any>(dataChart);

  let chart;
  const scale = {
    time: {
      alias: '时间',
      type: 'time',
      mask: 'MM:ss',
      tickCount: 10,
      nice: false,
    },
    remain: {
      alias: 'remain',
    },
  };

  useEffect(() => {
    setInterval(() => {
      const now = new Date();
      const time = now.getTime();
      // eslint-disable-next-line no-bitwise
      const temperature1 = ~~(Math.random() * 5) + 22;
      const test = [] as any;
      test.push({
        time,
        remain: temperature1,
      });
      setTopupChart([...dataChart, ...test]);
    }, 2000);
  }, [dataChart, setTopupChart]);

  return (
    <div>
      <List.Item>
        <Card bodyStyle={{ margin: 0, padding: 0 }}>
          <h4 style={{ margin: 20 }}>{store}</h4>
          <Linerealtime />
        </Card>
        <Divider plain>***</Divider>
      </List.Item>
    </div>
  );

  return (
    <div>
      <List.Item>
        <Card bodyStyle={{ margin: 0, padding: 0 }}>
          <h4 style={{ margin: 20 }}>{store}</h4>
          <Chart
            height={300}
            autoFit
            data={topupChart}
            appendPadding={[20, 0]}
            scale={scale}
            onGetG2Instance={(g2Chart: any) => {
              chart = g2Chart;
            }}
          >
            <Tooltip />
            {data.length !== 0 ? <Axis /> : ''}
            <Legend />
            <Geom
              type="line"
              position="time*remain"
              color={['type', ['#ff7f0e', '#2ca02c']]}
              shape="smooth"
              size={1}
            />
          </Chart>
        </Card>
        <Divider plain>***</Divider>
      </List.Item>
    </div>
  );
};

interface AnalysisProps {
  dashboardTopup: any;
  dispatch: Dispatch<any>;
  loading: boolean;
}

const Analysis: React.FC<AnalysisProps> = (props) => {
  const { dispatch, dashboardTopup } = props;
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
        return GROUP_BALANCING_TOPUP.includes(store);
      });
      const arrData = [] as any;
      listFilterBalancing.forEach((element: any) => {
        const { store, data = {} } = (element as any) || {};
        const dataChart = [] as any;
        const { RemainData = '' } = data || {};
        if (RemainData && typeof RemainData === 'string') {
          const remainData = JSON.parse(RemainData) || [];
          remainData.forEach((elementRemain: any) => {
            const { time, remain } = elementRemain || {};
            dataChart.push({ time, remain });
          });
        }
        // dataChart.sort((item1: any, item2: any) => {
        //   return Number(item1.amount) - Number(item2.amount);
        // });
        arrData.push({ store, dataChart });
      });
      setAnalysis(arrData);
      setLoading(false);
    },
    [setLoading, setAnalysis],
  );

  const test = () => {
    let headers = new Headers();

    headers.append('Content-Type', 'application/json');
    headers.append('Accept', 'application/json');
    headers.append('Authorization', 'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1c2VyIjoiMDkxNDExMTExMSIsIk5BTUUiOiJBbmggRHVjIDEiLCJpbWVpIjoiNmU5MzYxZDgtNzY1My00ZDM0LWI4ZTQtMjQzNTlkZjA3NzIxIiwiYWdlbnRfaWQiOjM3MDA4NDI3LCJCQU5LX0NPREUiOiIzMDEiLCJCQU5LX05BTUUiOiJWaWV0Y29tYmFuayIsIlZBTElEX1dBTExFVF9DT05GSVJNIjoxLCJNQVBfU0FDT01fQ0FSRCI6MCwiSURFTlRJRlkiOiJDT05GSVJNIiwiREVWSUNFX09TIjoiQU5EUk9JRCIsInNlc3Npb25LZXkiOiJET0lzRFo4L2ZQeDJuc001NzRtRXE0TGxXL0J5SUhSWWt1Q1Z0NFFYdTdaajJaV3kxNHdBdWc9PSIsInBpbiI6Ii9wNVR3a3FnalFZPSIsInBpbl9lbmNyeXB0Ijp0cnVlLCJ2ZXJpZnlJbmZvIjoiMTIzNCIsImJhbmtWZXJpZnlOYW1lIjoiUEhBVCBEQVQiLCJiYW5rVmVyaWZ5UGVyc29uYWxJZCI6IjEyMzQ1NiIsImxldmVsIjowLCJBUFBfVkVSIjozMDAwMiwiaWF0IjoxNjEwODgxMzU0fQ.Jm-rVFZIFCacoXQ9kppwNAAHGe8DZNT3BDMeSLDqem5I881YN1iM9nRQC-avnayy2xQZTln1GzBnPBbAjl8Dqq1sDRub70jGAhy4HDKlTBLNNyznrztNrxdQnT4zhSGW9YIurGOvvYhTqi288P25SuP0Os2-h-NubtOkoo8uQxp7t1baSC2ybTElo9OybI-otNmCzr2RRbBg5rQf_FGheeI11E5l2A5gVwRXaq7rKZsYZN8KxVk_hSkB0Na4H9q7ZV_BOw8ufEgvLBSKdXcngTfPIutPKt2rwLopECPns4fUDm-MaDqrz7Q2JHy3VqeTHRpPLz7x-5_KwPKlWdq-Ww');
    headers.append('Origin','http://localhost:8000');

    fetch('http://api-dev.mservice.io:22454/web-tool-app-http-billpay-telco/telco/webTool/getBalancing', {
        mode: 'cors',
        credentials: 'include',
        method: 'get',
        headers: headers
    })
    .then(response => response.json())
    .then(json => console.log(json))
    .catch(error => console.log('Authorization failed : ' + error.message));
  }

  useEffect(() => {
    test();
    // if (dispatch) {
    //   reqRef.current = requestAnimationFrame(() => {
    //     dispatch({
    //       type: 'dashboardTopup/getBalancing',
    //       params: {},
    //     });
    //   });
    // }
    return () => {};
  }, [dispatch]);

  useLayoutEffect(() => {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    useDataAnalysis(dashboardTopup);

    return () => {};
  }, [dashboardTopup, useDataAnalysis]);
  return (
    <div>
      <Spin spinning={isShowSpin}>
        <List
          grid={{ gutter: 10, column: 2 }}
          dataSource={dataAnalysis}
          renderItem={(item: any) => <ItemAnalysis key={Math.random()} data={item} />}
        />
      </Spin>
    </div>
  );
};

export default connect(
  ({
    dashboardTopup,
    loading,
  }: {
    dashboardTopup: any;
    loading: {
      effects: { [key: string]: boolean };
    };
  }) => ({
    dashboardTopup,
    loading: loading.effects['dashboardTopup/getBalancing'],
  }),
)(Analysis);
