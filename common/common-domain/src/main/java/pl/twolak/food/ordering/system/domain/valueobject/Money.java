package pl.twolak.food.ordering.system.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {

    private final BigDecimal amount;

    public Money(BigDecimal amount) {

        this.amount = amount;
    }

    public boolean isGreaterThanZero() {

        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isGreaterThan(Money money) {

        return amount != null && amount.compareTo(money.amount) > 0;
    }

    public Money add(Money money) {

        BigDecimal sum = setScale(amount.add(money.amount));

        return new Money(sum);
    }

    public Money subtract(Money money) {

        BigDecimal subtract = setScale(amount.subtract(money.amount));

        return new Money(subtract);
    }

    public Money multiply(int multiplier) {

        BigDecimal result = setScale(amount.multiply(new BigDecimal(multiplier)));

        return new Money(result);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    private BigDecimal setScale(BigDecimal input) {

        return input.setScale(2, RoundingMode.HALF_EVEN);
    }
}
