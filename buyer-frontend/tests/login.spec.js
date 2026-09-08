import { test, expect } from '@playwright/test';

test('ورود موفق با نام کاربری و رمز عبور 1234', async ({ page }) => {
    await page.goto('http://localhost:3000/login');
    await page.fill('input[placeholder="نام کاربری را وارد کنید"]', '1234');
    await page.fill('input[placeholder="رمز عبور را وارد کنید"]', '1234');
    await page.click('button:has-text("ورود / ثبت نام")');
    await expect(page.locator('text=خروج')).toBeVisible();
});
