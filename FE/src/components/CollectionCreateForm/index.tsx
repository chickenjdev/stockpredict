/* eslint-disable @typescript-eslint/no-shadow */
/* eslint-disable react-hooks/rules-of-hooks */
/* eslint-disable @typescript-eslint/no-use-before-define */
/* eslint-disable @typescript-eslint/no-unused-vars */
/* eslint-disable-next-line react-hooks/rules-of-hooks */

import type { FC } from 'react';
import React, { useEffect, useLayoutEffect, useCallback, useRef } from 'react';
import { Modal, Input, Form, Button } from 'antd';

interface CollectionCreateFormProps {
  visible: boolean;
  recordAll: any;
  onCancel?: Function;
  onCreate?: Function;
}

const CollectionCreateForm: FC<CollectionCreateFormProps> = ({
  visible,
  recordAll,
  onCancel,
  onCreate,
}: any) => {
  const description = useRef<any>(null);
  const [form] = Form.useForm();
  const myId = `${Math.random()}`;
  const { record = [], serviceId, offProvider } = recordAll || {};

  const useCancel = useCallback(() => {
    if (onCancel && typeof onCancel === 'function') {
      onCancel();
    }
    form.resetFields();
  }, [form, onCancel]);

  const onFinish = useCallback(() => {
    if (onCreate && typeof onCreate === 'function') {
      onCreate(offProvider, serviceId, record, description.current || '');
      form.resetFields();
    }
  }, [form, offProvider, onCreate, record, serviceId]);

  const useChange = useCallback((value: any) => {
    description.current = value.target.value;
  }, []);
  let title;
  if (record) {
    const firstItem = record && record[0];
    const { active } = firstItem || {};
    const arrPrice: any[] = [];
    record.forEach((item: any) => {
      const { price } = item || {};
      arrPrice.push(price);
    });
    title =
      offProvider === null
        ? `Bạn muốn ${active ? 'OFF' : 'ON'} mệnh giá ${arrPrice}?`
        : `Bạn muốn ${offProvider ? 'bảo trì' : 'mở'}  dịch vụ ${serviceId}`;
  }

  useEffect(() => {
    return () => {};
  }, []);

  useLayoutEffect(() => {
    return () => {};
  }, []);

  return (
    <>
      <Modal
        visible={visible}
        title={title || ''}
        closable={false}
        footer={
          <div
            style={{
              textAlign: 'right',
            }}
          >
            <Button onClick={useCancel} style={{ marginRight: 8 }}>
              Cancel
            </Button>
            <Button type="primary" form={myId} htmlType="submit">
              Submit
            </Button>
          </div>
        }
      >
        <Form
          id={myId}
          form={form}
          layout="vertical"
          name="control-hooks"
          requiredMark={true}
          onFinish={onFinish}
        >
          <Form.Item
            name={serviceId}
            label={serviceId}
            rules={[
              {
                required: true,
                message: 'Please enter description!',
              },
            ]}
            initialValue={''}
          >
            <Input.TextArea rows={4} placeholder="Ghi chú..." onChange={useChange} />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};

export default CollectionCreateForm;
