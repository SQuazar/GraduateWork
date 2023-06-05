import React from 'react';
import {Select} from "antd";

const CategorySelect = ({categories, selectedCategories, onChange}) => {
    const handleCategoriesChange = (option) => {
        onChange(option.map(o => {return {id: o.value, name: o.label}}))
    }

    return (
        <Select
            key="categories"
            options={categories?.map(category => {
                return {value: category.id, label: category.name}
            })}
            defaultValue={selectedCategories?.map(category => {
                return {value: category.id, label: category.name}
            })}
            onChange={(value, option) => handleCategoriesChange(option)}
            allowClear
            mode="multiple"
            style={{
                width: '100%'
            }}
        />
    );
};

export default CategorySelect;