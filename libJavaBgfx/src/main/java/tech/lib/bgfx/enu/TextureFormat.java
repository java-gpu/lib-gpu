package tech.lib.bgfx.enu;

import lombok.Getter;

@Getter
public enum TextureFormat {
    BC1(0),
    BC2(1),
    BC3(2),
    BC4(3),
    BC5(4),
    BC6H(5),
    BC7(6),
    ETC1(7),
    ETC2(8),
    ETC2A(9),
    ETC2A1(10),
    PTC12(11),
    PTC14(12),
    PTC12A(13),
    PTC14A(14),
    PTC22(15),
    PTC24(16),
    ATC(17),
    ATCE(18),
    ATCI(19),
    ASTC4x4(20),
    ASTC5x4(21),
    ASTC5x5(22),
    ASTC6x5(23),
    ASTC6x6(24),
    ASTC8x5(25),
    ASTC8x6(26),
    ASTC8x8(27),
    ASTC10x5(28),
    ASTC10x6(29),
    ASTC10x8(30),
    ASTC10x10(31),
    ASTC12x10(32),
    ASTC12x12(33),
    Unknown(34),

    R1(35),
    A8(36),
    R8(37),
    R8I(38),
    R8U(39),
    R8S(40),
    R16(41),
    R16I(42),
    R16U(43),
    R16F(44),
    R16S(45),
    R32I(46),
    R32U(47),
    R32F(48),
    RG8(49),
    RG8I(50),
    RG8U(51),
    RG8S(52),
    RG16(53),
    RG16I(54),
    RG16U(55),
    RG16F(56),
    RG16S(57),
    RG32I(58),
    RG32U(59),
    RG32F(60),
    RGB8(61),
    RGB8I(62),
    RGB8U(63),
    RGB8S(64),
    RGB9E5F(65),
    BGRA8(66),
    RGBA8(67),
    RGBA8I(68),
    RGBA8U(69),
    RGBA8S(70),
    RGBA16(71),
    RGBA16I(72),
    RGBA16U(73),
    RGBA16F(74),
    RGBA16S(75),
    RGBA32I(76),
    RGBA32U(77),
    RGBA32F(78),
    B5G6R5(79),
    R5G6B5(80),
    BGRA4(81),
    RGBA4(82),
    BGR5A1(83),
    RGB5A1(84),
    RGB10A2(85),
    RG11B10F(86),

    UnknownDepth(87),

    D16(88),
    D24(89),
    D24S8(90),
    D32(91),
    D16F(92),
    D24F(93),
    D32F(94),
    D0S8(95);

    private final int value;

    TextureFormat(int value) {
        this.value = value;
    }

    // You can add a method to map from integer to TextureFormat if needed
    public static TextureFormat fromValue(int value) {
        for (TextureFormat format : TextureFormat.values()) {
            if (format.getValue() == value) {
                return format;
            }
        }
        return null; // Or throw an exception if invalid value
    }
}
