package pl.harshita_gupta.trackexpensesappbackend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.validation.annotation.Validated;

import pl.harshita_gupta.trackexpensesappbackend.TestUtils;
import pl.harshita_gupta.trackexpensesappbackend.auth.api.AuthRepository;
import pl.harshita_gupta.trackexpensesappbackend.auth.usermodel.User;
import pl.harshita_gupta.trackexpensesappbackend.general.exception.AppRuntimeException;
import pl.harshita_gupta.trackexpensesappbackend.general.exception.ErrorCode;
import pl.harshita_gupta.trackexpensesappbackend.general.exception.ErrorStrategy;
import pl.harshita_gupta.trackexpensesappbackend.wallet.WalletController;
import pl.harshita_gupta.trackexpensesappbackend.wallet.api.WalletModelMapper;
import pl.harshita_gupta.trackexpensesappbackend.wallet.api.WalletRepository;
import pl.harshita_gupta.trackexpensesappbackend.wallet.impl.WalletServiceImpl;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Validated
@WebMvcTest(
        controllers = WalletController.class,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {WalletRepository.class, WalletServiceImpl.class}))
class WalletDeleteServiceImplTest {

    public static final long ID_1L = 1L;

    public static final long ID_5L = 5L;

    private static final String NAME_1 = "wallet name one";

    private static final Instant DATE_NOW = Instant.now();
    private static final Long USER_ID_1L = 1L;

    @MockBean
    private ErrorStrategy errorStrategy;

    @MockBean
    private WalletModelMapper walletModelMapper;

    @MockBean
    private WalletRepository walletRepository;

    @MockBean
    private AuthRepository userRepository;

    @Autowired
    private WalletServiceImpl walletService;

    @Test
    @DisplayName("when wallet with id does not exist should not delete wallet")
    void shouldNotDeleteWallet_WhenWalletWithIdDoesNotExist() {
        //given
        User user = TestUtils.createUserForTest(1L);

        //when
        when(walletRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        //then
        AppRuntimeException appRuntimeException = assertThrows(
                AppRuntimeException.class,
                () -> walletService.deleteWalletById(ID_5L, USER_ID_1L));

        assertAll(
                () -> assertEquals(ErrorCode.W003.getBusinessStatusCode(), appRuntimeException.getBusinessStatusCode()),
                () -> assertEquals(ErrorCode.W003.getBusinessMessage(), appRuntimeException.getBusinessMessage()),
                () -> assertEquals(ErrorCode.W003.getBusinessStatus(), appRuntimeException.getBusinessStatus())
        );

    }

}
