import { test, expect } from '@playwright/test';

test.describe('فیلتر و جستجوی محصولات', () => {

    test('فیلتر → حذف فیلتر → جستجو', async ({ page }) => {
        await page.goto('http://localhost:3000');

        // اطمینان از وجود محصولات اولیه
        let productCount = await page.locator('.product-card').count();
        expect(productCount).toBeGreaterThan(0);

        // باز کردن منوی فیلتر و انتخاب اولین نوع محصول
        await page.hover('text=فیلتر محصولات');
        const firstType = page.locator('.filter-section').first().locator('ul li').first();
        const selectedTypeName = await firstType.textContent();
        await firstType.click();

        // بررسی اعمال فیلتر
        productCount = await page.locator('.product-card').count();
        expect(productCount).toBeGreaterThan(0);
        const filteredProducts = await page.locator('.product-card').allTextContents();
        expect(filteredProducts.join(' ')).toContain(selectedTypeName.trim());

        // حذف فیلتر
        await page.hover('text=فیلتر محصولات');
        await page.click('text=حذف فیلترها');
        productCount = await page.locator('.product-card').count();
        expect(productCount).toBeGreaterThan(0);

        // جستجو
        const searchInput = page.locator('[data-testid="search-input"]');
        await searchInput.fill('iphone');
        await expect(page.locator('.product-card')).toContainText(/iphone/i);
    });

});
