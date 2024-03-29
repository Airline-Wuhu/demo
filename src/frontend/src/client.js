import fetch from 'unfetch';

const checkStatus = response => {
    if (response.ok) {
        return response;
    }
    // return error
    const error = new Error(response.statusText);
    error.response = response;
    return Promise.reject(error);
}
export const getAllStudents = () =>
    fetch("api/v1/students")
        .then(checkStatus);

export const addNewStudent = student =>
    fetch("api/v1/students", {
            headers: {
                'Content-Type': 'application/json'
            },
            method: 'POST',
            body: JSON.stringify(student)
        }
    ).then(checkStatus);
export const deleteStudent = studentId =>
    fetch(`api/v1/students/${studentId}`, {
            method: 'DELETE'
        }).then(checkStatus);
export const updateStudent = (studentId, student) =>
    fetch(`api/v1/students/${studentId}`, {
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(student),
        method: 'PUT'
        }).then(checkStatus);


