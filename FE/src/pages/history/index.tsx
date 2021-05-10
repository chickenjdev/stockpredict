/* eslint-disable @typescript-eslint/no-use-before-define */
/* eslint-disable @typescript-eslint/no-unused-vars */
import type { FC } from 'react';
import React, { useEffect, useRef, useLayoutEffect, useCallback, useState } from 'react';
import type { Dispatch } from 'umi';
import { connect } from 'umi';
import { Button, Input, Space, Table, Typography } from 'antd';
import { SearchOutlined } from '@ant-design/icons';
import moment from 'moment';
const { TextArea } = Input;
const { Text } = Typography;

interface HistoryProps {
  loading: boolean;
  dispatch: Dispatch<any>;
  history: any;
}

const History: FC<HistoryProps> = ({ dispatch, history }) => {
  const reqRef = useRef<number>(0);
  const pageNumber = useRef<number>(1);
  const searchInput = useRef<Input>(null);
  const [mHistory, setHistory] = useState<any>([]);
  const [loading, setLoading] = useState(false);
  const saveHistory = useRef<any>([]);

  const handleSearch = (selectedKeys: any[], confirm: () => void, dataIndex: any): any => {
    confirm();
    // this.setState({
    //   searchText: selectedKeys[0],
    //   searchedColumn: dataIndex,
    // });
  };

  const handleReset = (clearFilters: () => void) => {
    clearFilters();
    // this.setState({ searchText: '' });
  };

  const getColumnSearchProps = (dataIndex: any) => ({
    filterDropdown: ({ setSelectedKeys, selectedKeys, confirm, clearFilters }: any) => (
      <div style={{ padding: 8 }}>
        <Input
          ref={searchInput}
          placeholder={`Search ${dataIndex}`}
          value={selectedKeys[0]}
          onChange={(e) => setSelectedKeys(e.target.value ? [e.target.value] : [])}
          onPressEnter={() => handleSearch(selectedKeys, confirm, dataIndex)}
          style={{ width: 188, marginBottom: 8, display: 'block' }}
        />
        <Space>
          <Button
            type="primary"
            onClick={() => handleSearch(selectedKeys, confirm, dataIndex)}
            icon={<SearchOutlined />}
            size="small"
            style={{ width: 90 }}
          >
            Search
          </Button>
          <Button onClick={() => handleReset(clearFilters)} size="small" style={{ width: 90 }}>
            Reset
          </Button>
        </Space>
      </div>
    ),
    filterIcon: (filtered: any) => (
      <SearchOutlined style={{ color: filtered ? '#1890ff' : undefined }} />
    ),
    onFilter: (value: string, record: Record<string, { toString: () => string }>) =>
      record[dataIndex]
        ? record[dataIndex].toString().toLowerCase().includes(value.toLowerCase())
        : '',
    onFilterDropdownVisibleChange: (visible: any) => {
      if (visible) {
        setTimeout(() => searchInput.current && searchInput.current.select(), 100);
      }
    },
    render: (text: any) => {
      return (
        <Text style={{ width: 200, maxWidth: 100, maxLines: 2 }} strong>
          {text && text.replace('@mservice.com.vn', '') || ""}
        </Text>
      );
    },
  });

  const columns = [
    {
      title: 'Username',
      dataIndex: 'username',
      key: 'username',
      width: 200,
      // align: 'center',
      ...getColumnSearchProps('username'),
    },
    {
      title: 'Old Data',
      dataIndex: 'oldData',
      key: 'oldData',
      align: 'center',
      render: (text: any) => {
        return <TextArea style={{ width: 200 }} rows={2} value={text} />;
      },
    },
    {
      title: 'New Data',
      dataIndex: 'newData',
      key: 'newData',
      align: 'center',
      render: (text: any) => {
        return <TextArea style={{ width: 200 }} rows={2} value={text} />;
      },
    },
    {
      title: 'Description',
      dataIndex: 'desc',
      key: 'desc',
      align: 'center',
    },
    {
      title: 'Last Update',
      dataIndex: 'lastUpdate',
      key: 'lastUpdate',
      align: 'center',
      render: (text: any) => {
        return <Text> {moment(text).locale('en').format('MM/DD/YYYY, h:mm A')}</Text>;
      },
    },
  ];

  const useLoadMore = useCallback(() => {
    if (dispatch) {
      // eslint-disable-next-line no-multi-assign
      pageNumber.current = pageNumber.current += 1;
      setLoading(true);
      reqRef.current = requestAnimationFrame(() => {
        dispatch({
          type: 'history/fetchHistory',
          params: {
            pageNumber: pageNumber.current,
          },
          callback: (response: any) => {
            // eslint-disable-next-line react-hooks/rules-of-hooks
            useHistory(response);
          }
        });
      });
    }
  }, [dispatch, useHistory]);

  const footer = () => {
    return (
      <Button type="primary" loading={loading} onClick={useLoadMore}>
        Load more
      </Button>
    );
  };

  // eslint-disable-next-line react-hooks/exhaustive-deps
  const useHistory = useCallback(
    (listHistory: any) => {
      const { dataHistory } = listHistory || {};
      const { data } = dataHistory || {};
      const { listHistoryData = [] } = data || {};
      if (listHistoryData) {
        const save = [...saveHistory.current, ...listHistoryData];
        setHistory(save);
        saveHistory.current = save;
      }
      setTimeout(() => {
        setLoading(false);
      }, 1000);
    },
    [setHistory, setLoading],
  );

  useEffect(() => {
    if (dispatch) {
      reqRef.current = requestAnimationFrame(() => {
        dispatch({
          type: 'history/fetchHistory',
          params: {
            pageNumber: pageNumber.current,
          },
        });
      });
    }
    return () => {
      cancelAnimationFrame(reqRef.current);
    };
  }, [dispatch,]);

  useLayoutEffect(() => {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    useHistory(history);
    return () => {
      saveHistory.current = [];
      setHistory([]);
    };
  }, [useHistory, history]);

  return (
    <>
      <div>
        <Table
          columns={columns}
          dataSource={mHistory}
          bordered={true}
          pagination={{
            hideOnSinglePage: true,
            // position: [],
          }}
          tableLayout={'fixed'}
          footer={footer}
        />
      </div>
    </>
  );
};

export default connect(
  ({
    history,
    loading,
  }: {
    history: any;
    loading: {
      effects: { [key: string]: boolean };
    };
  }) => ({
    history,
    loading: loading.effects['history/fetchHistory'],
  }),
)(History);
