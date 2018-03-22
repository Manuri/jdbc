function testElvisValueTypePositive () returns (int) {
    int|null x = 120;
    int b;
    b = x ?: 111;
    return b;
}

function testElvisValueTypeNegative () returns (int) {
    int|null x = null;
    int b;
    b = x ?: 111;
    return b;
}

function testElvisValueTypeNested () returns (int) {
    int|null x = null;
    int|null y = 3000;
    int b;
    b = x ?: (y ?: 1300);
    return b;
}

function testElvisRefTypePositive () returns (int|string) {
    int|string|null x = 2300;
    int|string b;
    b = x ?: 111;
    return b;
}

function testElvisRefTypeNegative () returns (int|string) {
    int|string|null x = null;
    int|string b;
    b = x ?: 111;
    return b;
}

