import {
    DEFAULT_MONEY_DECIMAL_DELIMITER,
    DEFAULT_SIGNS_COUNT_AFTER_DELIMITER
} from "./constants";

export const formatByMoneyPattern = (value: number, rerurnDecimals?: boolean): string => {
    const [wholePart, fractionalPart] = value
        .toFixed(DEFAULT_SIGNS_COUNT_AFTER_DELIMITER)
        .split(DEFAULT_MONEY_DECIMAL_DELIMITER);
    const formattedWholePart = Array
        .from(wholePart.toString())
        .reverse()
        .reduce(
            (acc, num, i) => {
                if (i % 3 === 0) {
                    acc.push(' ', num);
                } else {
                    acc.push(num);
                }
                return acc;
            }, [] as string[]
        )
        .reverse()
        .join('')
    return rerurnDecimals ? [formattedWholePart, fractionalPart].join(DEFAULT_MONEY_DECIMAL_DELIMITER):formattedWholePart
};