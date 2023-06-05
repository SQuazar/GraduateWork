import React from 'react';
import {Select} from "antd";

const RolesSelect = ({roles, selectedRoles, onChange}) => {
    const handleRolesChange = (option) => {
        onChange(option.map(o => {return {id: o.value, name: o.label}}))
    }

    return (
        <Select
            key="roles"
            options={roles?.map(role => {
                return {value: role.id, label: role.name}
            })}
            defaultValue={selectedRoles?.map(role => {
                return {value: role.id, label: role.name}
            })}
            onChange={(value, option) => handleRolesChange(option)}
            allowClear
            mode="multiple"
            style={{
                width: '100%'
            }}
        />
    );
};

export default RolesSelect;