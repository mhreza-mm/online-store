import React, { useState, useEffect } from "react";

export default function FilterBar({ products, onFilter }) {
    const [types, setTypes] = useState([]);
    const [brands, setBrands] = useState([]);
    const [selectedType, setSelectedType] = useState("");
    const [selectedBrand, setSelectedBrand] = useState("");

    useEffect(() => {
        if (products && products.length) {
            const uniqueTypes = [...new Set(products.map(p => p.type))];
            setTypes(uniqueTypes);
        }
    }, [products]);

    useEffect(() => {
        if (selectedType) {
            const filteredBrands = [
                ...new Set(products.filter(p => p.type === selectedType).map(p => p.brand))
            ];

            setBrands(filteredBrands);

            setSelectedBrand(prev =>
                filteredBrands.includes(prev) ? prev : ""
            );
        } else {
            setBrands([]);
        }
    }, [selectedType, products]);

    useEffect(() => {
        onFilter(selectedType, selectedBrand);
    }, [selectedType, selectedBrand]);

    const clearFilters = () => {
        setSelectedType("");
        setSelectedBrand("");
        onFilter("", "");
    };

    return (
        <div className="market-menu">
            <span className="market-title">
                فیلتر محصولات
                <span className="filter-icon">▾</span>
            </span>

            <div className="filter-dropdown">
                <div className="filter-section">
                    <h4>نوع محصول</h4>
                    <ul>
                        {types.map(type => (
                            <li
                                key={type}
                                className={selectedType === type ? "active" : ""}
                                onClick={() => setSelectedType(type)}
                            >
                                {type}
                            </li>
                        ))}
                    </ul>
                </div>

                <div className="filter-section">
                    <h4>برند</h4>
                    <ul>
                        {brands.map(brand => (
                            <li
                                key={brand}
                                className={selectedBrand === brand ? "active" : ""}
                                onClick={() => setSelectedBrand(brand)}
                            >
                                {brand}
                            </li>
                        ))}
                    </ul>
                </div>

                <button onClick={clearFilters} className="clear-filters-btn">
                    حذف فیلترها
                </button>
            </div>
        </div>
    );
}
