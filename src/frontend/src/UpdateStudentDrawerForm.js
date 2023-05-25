
import {Button, Col, Drawer, Form, Input, Row, Select, Spin} from 'antd';
import {updateStudent} from "./client";
import {LoadingOutlined} from "@ant-design/icons";
import {useState} from "react";
import {successNotificationWithIcon, errorNotificationWithIcon} from "./Notification"
const { Option } = Select;

const spinner = <LoadingOutlined
    style={{
        fontSize: 24,
    }}
    spin
/>

function UpdateStudentDrawerForm({showUpdateDrawer, setShowUpdateDrawer, fetchStudents, studentID}) {
    const [submitting, setSubmitting] = useState(false);
    const onClose = () => {
        setShowUpdateDrawer(false);
    };
    const onFinish = values => {
        setSubmitting(true)
        console.log(JSON.stringify(values, null, 2));
        updateStudent(studentID, values)
            .then(() => {
            console.log("student updated")
            onClose();
            successNotificationWithIcon(
                "Student updated",
                `the student with id: ${studentID} updated.`
            )
            fetchStudents();
        }).catch(err => {
            console.log(err);
            err.response.json().then(res => {
                console.log(res);
                errorNotificationWithIcon(
                    "Something happened",
                    `${res.message} [${res.status}] [${res.error}]`,
                    "bottomLeft"
                )
            });
        }).finally(() => {
            setSubmitting(false);
        })

    };
    const onFinishFailed = errorInfo => {
        alert(JSON.stringify(errorInfo, null, 2));
    };
    return (
        <>
            <Drawer
                title="Enter Information of the student"
                width={720}
                onClose={onClose}
                visible={showUpdateDrawer}
                bodyStyle={{
                    paddingBottom: 80,
                }}
                footer={
                    <div
                        style={{
                            textAlign: 'right',
                        }}
                    >
                        <Button onClick={onClose} style={{marginRight: 8}}>Cancel</Button>
                    </div>
                }
            >
                <Form layout="vertical"
                        onFinishFailed={onFinishFailed}
                        onFinish={onFinish}
                >
                    <Row gutter={16}>
                        <Col span={12}>
                            <Form.Item
                                name="name"
                                label="Name"
                                rules={[
                                    {
                                        required: true,
                                        message: 'Please enter user name',
                                    },
                                ]}
                            >
                                <Input placeholder="Please enter user name" />
                            </Form.Item>
                        </Col>
                        <Col span={12}>
                            <Form.Item
                                name="email"
                                label="Email"
                                rules={[
                                    {
                                        required: true,
                                        message: 'Please enter Email',
                                    },
                                ]}
                            >
                                <Input
                                    style={{
                                        width: '100%',
                                    }}
                                    placeholder="Please enter Email"
                                />
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                        <Col span={12}>
                            <Form.Item
                                name="gender"
                                label="Gender"
                                rules={[
                                    {
                                        required: true,
                                        message: 'Please select a gender',
                                    },
                                ]}
                            >
                                <Select placeholder="Please select a gender">
                                    <Option value= "MALE">Male</Option>
                                    <Option value= "FEMALE">Female</Option>
                                    <Option value= "OTHERS">Others</Option>
                                </Select>
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row>
                        <Col span={12}>
                            <Form.Item>
                                <Button type="primary" htmlType="submit">
                                    Submit
                                </Button>
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row>
                        {submitting && <Spin indicator={spinner}/>}
                    </Row>
                </Form>
            </Drawer>
        </>
    );
}
export default UpdateStudentDrawerForm;