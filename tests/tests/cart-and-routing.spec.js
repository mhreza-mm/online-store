import { test, expect } from '@playwright/test';

test.describe('مدیریت سبد خرید و ناوبری', () => {

    // لاگین قبل از هر تست
    test.beforeEach(async ({ page }) => {
        await page.goto('http://localhost:3000/login');
        await page.fill('input[placeholder="نام کاربری را وارد کنید"]', '1234');
        await page.fill('input[placeholder="رمز عبور را وارد کنید"]', '1234');
        await page.click('button:has-text("ورود / ثبت نام")');
        await expect(page.locator('text=خروج')).toBeVisible({ timeout: 5000 });
    });

    // 1️⃣ افزودن یک محصول به سبد خرید
    test('افزودن یک محصول به سبد خرید', async ({ page }) => {
        await page.waitForSelector('.product-card', { state: 'visible', timeout: 10000 });

        const firstProduct = page.locator('.product-card').first();
        const addToCartBtn = firstProduct.locator('button:has-text("افزودن به سبد خرید")');

        if (await addToCartBtn.count() > 0) {
            await addToCartBtn.waitFor({ state: 'visible', timeout: 7000 });
            await addToCartBtn.click();
        } else {
            const plusBtn = firstProduct.locator('button:has-text("+")');
            await plusBtn.waitFor({ state: 'visible', timeout: 7000 });
            await plusBtn.click();
        }

        const cartCount = await page.locator('.navbar-cart span').textContent();
        expect(parseInt(cartCount)).toBe(1);
    });

    // 2️⃣ افزودن چند محصول و بررسی جمع کل قیمت

    // 4️⃣ ناوبری بین صفحات
    test('ناوبری بین صفحات', async ({ page }) => {
        await page.click('.navbar-cart');
        await expect(page).toHaveURL(/.*cart/);

        await page.click('.navbar-logo');
        await expect(page).toHaveURL('http://localhost:3000/');

        const loginButton = page.locator('text=ورود | ثبت‌نام');
        if (await loginButton.count() > 0) {
            await loginButton.click();
            await expect(page).toHaveURL(/.*login/);
        }
    });
});

