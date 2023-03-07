import {notification} from "antd";

const openNotificationWithIcon = (type, msg, description, placement) => {
    placement = placement || "topRight"
    notification[type]({

        message: msg,
        description:
            description,
        placement,
    });
};
export const successNotificationWithIcon = (msg, description, placement) =>
    openNotificationWithIcon('success', msg, description, placement);


export const errorNotificationWithIcon = (msg, description, placement) =>
    openNotificationWithIcon('error', msg, description, placement);

export const infoNotificationWithIcon = (msg, description, placement) =>
    openNotificationWithIcon('info', msg, description, placement);

export const warningNotificationWithIcon = (msg, description, placement) =>
    openNotificationWithIcon('warning', msg, description, placement);
