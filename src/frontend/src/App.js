import React, { useState, useEffect } from "react";
import {deleteStudent, getAllStudents} from "./client";
import {
    CheckCircleOutlined,
    DesktopOutlined,
    FileOutlined,
    PieChartOutlined, PlusOutlined,
    TeamOutlined,
    UserOutlined,
} from '@ant-design/icons';



import {
    Empty,
    Alert,
    Spin,
    Table,
    Breadcrumb,
    Layout,
    Menu,
    Button,
    Badge,
    Tag,
    Avatar,
    Radio,
    notification,
    Popconfirm,
    Image
} from 'antd';
import SubMenu from "antd/es/menu/SubMenu";
import StudentDrawerForm from "./StudentDrawerForm";

import {errorNotificationWithIcon} from "./Notification";

const { Header, Content, Footer, Sider } = Layout;

/*
function getItem(label, key, icon, children) {
    return {
        key,
        icon,
        children,
        label,
    };
}

 */
const InitAvatar = ({name}) => {
    let trim = name.trim();
    if (trim.length === 0) {
        return <Avatar icon = {<UserOutlined/>}/>
    }
    const split = trim.split(" ");
    if (split.length === 1) {
        return <Avatar>{name.charAt(0)}</Avatar>
    } else {
        return <Avatar>{name.charAt(0)}{split[1].charAt(0)}</Avatar>
    }
}


const successNotification = (title, message) => {
    notification.open({
        message: title,
        description:
            message,
        icon: (
            <CheckCircleOutlined />
        ),
    });

};
const removeStudent = (studentId, callback) => {
    deleteStudent(studentId).then(() => {
        successNotification("Student deleted", `Student with ID: ${studentId} was deleted`);
        callback();
    }).catch(err => {
        err.response.json().then(res => {
            console.log(res);
            errorNotificationWithIcon("Something happened", `Student with ID: ${studentId} not found ${res.message} [statusCode: ${res.status}]`)
        });
    })

};

function App() {

    const [students, setStudents] = useState([]);
    const [collapsed, setCollapsed] = useState(false);
    const [fetching, setFetching] = useState(true);
    const [showDrawer, setShowDrawer] = useState(false);

    const columns = fetchStudents => [
        {
            title: 'Avatar',
            dataIndex: 'avatar',
            key: 'avatar',
            render: (text, student) => <InitAvatar name={student.name}/>
        },
        {
            title: 'ID',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Email',
            dataIndex: 'email',
            key: 'email',
        },
        {
            title: 'Gender',
            dataIndex: 'gender',
            key: 'gender',
        },
        {
            title: 'Actions',
            key: 'actions',
            render: (text, student) =>
                <Radio.Group>
                    <Popconfirm
                        placement='topRight'
                        title={`Are you sure to delete ${student.name}?`}
                        onConfirm = {() => removeStudent(student.id, fetchStudents)}
                        okText='Yes'
                        cancelText='No'>
                        <Radio.Button value="small">Delete</Radio.Button>
                    </Popconfirm>
                    <Radio.Button value="small">Edit</Radio.Button>
                </Radio.Group>


        },
    ];
    const fetchStudents = () =>
        getAllStudents()
            .then(res => res.json())
            .then(data => {
                console.log(data);
                setStudents(data);

            }).catch(err => {
                console.log(err.response)
                err.response.json().then(res => {
                    console.log(res);
                    errorNotificationWithIcon("Something happened", `${res.message} [statusCode: ${res.status}]`)
                });
        }).finally(() => setFetching(false))
    const renderStudents = () => {
        if (fetching) {
            return <Spin tip="Fetching data...">
                <Alert
                    message="No data available"
                    description="waiting for data "
                    type="info"
                />
            </Spin>
        }
        if (students.length <= 0) {
            return <>
                <Button
                    onClick={() => setShowDrawer(!showDrawer)}
                    type="primary" shape="round" icon={<PlusOutlined/>} size="small">
                    Add New Student
                </Button>
                <StudentDrawerForm
                    showDrawer={showDrawer}
                    setShowDrawer={setShowDrawer}
                    fetchStudents={fetchStudents}
                />
                <Empty/>
            </>
        }
        return <>
            <StudentDrawerForm
                showDrawer={showDrawer}
                setShowDrawer={setShowDrawer}
                fetchStudents={fetchStudents}
            />
            <Table
                dataSource={students}
                columns={columns(fetchStudents)}
                bordered
                title={() =>
                   <>
                    <Button
                    onClick={() => setShowDrawer(!showDrawer)}
                    type="primary" shape="round" icon={<PlusOutlined />} size='small'>
                    Add a new student
                    </Button>
                       <br/>
                       <br/>
                    <Tag>
                        Number of students
                    </Tag>
                   <Badge
                   className="site-badge-count"
                   count={students.length}
                   style={{ backgroundColor: 'gray' }}
                   />

                    </>
                }
            pagination={{ pageSize: 50 }} scroll={{ y: 400 }}
                rowKey={(student) => student.id}
            />
        </>
    }
    useEffect(() => {
        console.log("component is mounted");
        fetchStudents()
    }, [])


    return (
        <Layout
            style={{
                minHeight: '100vh',
            }}
        >
            <Sider collapsible collapsed={collapsed} onCollapse={(value) => setCollapsed(value)}>
                <div className="logo" />
                <Menu theme="dark" defaultSelectedKeys={['1']} mode="inline">
                    <Menu.Item key="1" icon={<PieChartOutlined />}>
                        Option 1
                    </Menu.Item>
                    <Menu.Item key="2" icon={<DesktopOutlined />}>
                        Option 2
                    </Menu.Item>
                    <SubMenu key="sub1" icon={<UserOutlined />} title={"User"}>
                        <Menu.Item key="3">
                            Tom
                        </Menu.Item>
                        <Menu.Item key="4">
                            Bill
                        </Menu.Item>
                        <Menu.Item key="5">
                            Alex
                        </Menu.Item>
                    </SubMenu>
                    <SubMenu key="sub2" icon={<TeamOutlined />} title={"Team"}>
                        <Menu.Item key="6">
                            Team 1
                        </Menu.Item>
                        <Menu.Item key="8">
                            Team 2
                        </Menu.Item>
                    </SubMenu>
                    <Menu.Item key="9" icon={<FileOutlined />}>
                        Files
                    </Menu.Item>
                </Menu>
            </Sider>
            <Layout className="site-layout">
                <Header
                    className="site-layout-background"
                    style={{
                        padding: 0,
                    }}
                />
                <Content
                    style={{
                        margin: '0 16px',
                    }}
                >
                    <Breadcrumb
                        style={{
                            margin: '16px 0',
                        }}
                    >
                        <Breadcrumb.Item>User</Breadcrumb.Item>
                        <Breadcrumb.Item>Bill</Breadcrumb.Item>
                    </Breadcrumb>
                    <div
                        className="site-layout-background"
                        style={{
                            padding: 24,
                            minHeight: 360,
                        }}
                    >
                        {renderStudents()}
                    </div>

                </Content>
                <Footer
                    style={{
                        textAlign: 'center',
                    }}
                >
                    This is a footer!
                    <br/>
                    <Image
                        width={35}
                        src="https://image11.m1905.cn/uploadfile/2016/0205/20160205103239628184.jpg"
                    />
                </Footer>
            </Layout>
        </Layout>

    );

}

export default App;
