import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ct.belfort.CandleDTO;
import ru.ct.belfort.MoneyValueDTO;
import ru.ct.belfort.PositionDataDTO;
import ru.ct.belfort.PositionsSecuritiesDTO;
import ru.ct.belfort.util.Utilities;
import ru.tinkoff.piapi.contract.v1.*;

public class UtilitiesTest {
    @Test
    void makeDoubleTest() {
        Assertions.assertEquals(Utilities.makeDouble(-0 , -10000000), -0.01);
        Assertions.assertEquals(Utilities.makeDouble(123 , 123456789), 123.123456789);
        Assertions.assertEquals(Utilities.makeDouble(1312323 , 12345678), 1312323.012345678);
        Assertions.assertEquals(Utilities.makeDouble(Quotation.newBuilder()
                .setUnits(123)
                .setNano(123456789)
                .build()), 123.123456789);
    }

    @Test
    void createCandleDTOTest() {
        Candle tinkoffCandle = Candle.newBuilder().
                setLow(
                        Quotation.newBuilder().
                                setUnits(53).
                                setNano(15).build()
                ).setHigh(
                        Quotation.newBuilder().
                                setUnits(123).
                                setNano(13214).build()
                ).setOpen(
                        Quotation.newBuilder().
                                setUnits(537).
                                setNano(123).build()
                ).setClose(
                        Quotation.newBuilder().
                                setUnits(1335).
                                setNano(5).build()
                ).setVolume(
                        888
                ).build();
        CandleDTO ourDto = Utilities.create(tinkoffCandle);

        Assertions.assertEquals(Utilities.makeDouble(tinkoffCandle.getLow()), ourDto.lowPrice());
        Assertions.assertEquals(Utilities.makeDouble(tinkoffCandle.getHigh()), ourDto.highPrice());
        Assertions.assertEquals(Utilities.makeDouble(tinkoffCandle.getOpen()), ourDto.openPrice());
        Assertions.assertEquals(Utilities.makeDouble(tinkoffCandle.getClose()), ourDto.closePrice());
        Assertions.assertEquals(tinkoffCandle.getVolume(), ourDto.volume());
    }

    @Test
    void createPositionDataDTOTest() {
        PositionData tinkoffData = PositionData.newBuilder()
                .setAccountId("some-account-id")
                .addMoney(
                        PositionsMoney.newBuilder().
                                setAvailableValue(MoneyValue.newBuilder().setUnits(64).setNano(12345)).build()
                ).addMoney(
                        PositionsMoney.newBuilder().
                                setAvailableValue(MoneyValue.newBuilder().setUnits(1).setNano(323)).build()
                ).addMoney(
                        PositionsMoney.newBuilder().
                                setAvailableValue(MoneyValue.newBuilder().setUnits(123).setNano(123)).build()
                ).addSecurities(PositionsSecurities.newBuilder()
                        .setFigi("some-cool-figi")
                        .setPositionUid("some-cool-puid")
                        .setInstrumentUid("some-cool-instrument-uid")
                        .setInstrumentType("some-cool-intrument-type"))
                .build();
        PositionDataDTO ourDto = Utilities.create(tinkoffData);

        Assertions.assertEquals(tinkoffData.getAccountId(), ourDto.accountId());
        Assertions.assertEquals(tinkoffData.getMoneyList().stream().map(PositionsMoney::getAvailableValue).
                        map(Utilities::create).toList(),
                ourDto.money());
        Assertions.assertEquals(tinkoffData.getSecuritiesList().stream().map(Utilities::create).toList(),
                ourDto.securities());
    }

    @Test
    void createPositionSecuritiesDTOTest() {
        PositionsSecurities tinkoffSecurities = PositionsSecurities.newBuilder()
                .setFigi("some-cool-figi")
                .setPositionUid("some-cool-position-uid")
                .setInstrumentUid("some-cool-instrument-uid")
                .setInstrumentType("some-cool-instrument-type")
                .build();
        PositionsSecuritiesDTO ourDto = Utilities.create(tinkoffSecurities);

        Assertions.assertEquals(tinkoffSecurities.getFigi(), ourDto.figi());
        Assertions.assertEquals(tinkoffSecurities.getPositionUid(), ourDto.positionUid());
        Assertions.assertEquals(tinkoffSecurities.getInstrumentUid(), ourDto.instrumentUid());
        Assertions.assertEquals(tinkoffSecurities.getInstrumentType(), ourDto.instrumentType());
    }

    @Test
    void createMoneyValueDTOTest() {
        MoneyValue tinkoffMoney = MoneyValue.newBuilder().setUnits(123432).setNano(123).build();
        MoneyValueDTO ourDto = Utilities.create(tinkoffMoney);

        Assertions.assertEquals(tinkoffMoney.getUnits(), ourDto.units());
        Assertions.assertEquals(tinkoffMoney.getNano(), ourDto.nano());
    }
}