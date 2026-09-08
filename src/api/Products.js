
export const fetchProducts = async ({ page = 0, size = 10, type = "", brand = "", search = "" }) => {
    try {
        const token = localStorage.getItem("token");
        let headers = token ? { "Authorization": `Bearer ${token}` } : {};

        const apiUrl = new URL("http://localhost:9090/api/products/paginated");
        apiUrl.searchParams.append("page", page);
        apiUrl.searchParams.append("size", size);
        if (type) apiUrl.searchParams.append("type", type);
        if (brand) apiUrl.searchParams.append("brand", brand);
        if (search) apiUrl.searchParams.append("search", search);

        let res = await fetch(apiUrl, { headers });

        if (res.status === 401 || res.status === 403) {
            console.warn("توکن معتبر نیست یا منقضی شده، درخواست بدون احراز هویت ارسال شد");
            res = await fetch(apiUrl);
        }

        if (!res.ok) {
            throw new Error(`خطا در دریافت محصولات: ${res.status} ${res.statusText}`);
        }

        const data = await res.json();
        return data;

    } catch (error) {
        console.error("خطا در fetchProducts:", error);
        return { content: [], totalPages: 0, totalElements: 0, page: 0, size: 10 };
    }
};


export const fetchProductById = async (id) => {
    try {
        const token = localStorage.getItem("token");
        let headers = token ? { "Authorization": `Bearer ${token}` } : {};

        const apiUrl = `http://localhost:9090/api/products/${id}`;

        let res = await fetch(apiUrl, { headers });

        if (res.status === 401 || res.status === 403) {
            console.warn("توکن معتبر نیست یا منقضی شده، درخواست بدون احراز هویت ارسال شد");
            res = await fetch(apiUrl);
        }

        if (!res.ok) {
            throw new Error(`خطا در دریافت محصول: ${res.status} ${res.statusText}`);
        }

        const data = await res.json();
        return data;

    } catch (error) {
        console.error("خطا در fetchProductById:", error);
        return null;
    }
};
