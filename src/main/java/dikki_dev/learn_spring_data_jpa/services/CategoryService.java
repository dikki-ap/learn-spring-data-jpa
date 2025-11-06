package dikki_dev.learn_spring_data_jpa.services;

import dikki_dev.learn_spring_data_jpa.entities.Category;
import dikki_dev.learn_spring_data_jpa.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionOperations;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /*
    -- Programmatic Transaction (Transaction Operations) --
    - Untuk kasus sederhana jika kita perlu "ASYNC" Transactional
    - Karena @Transactional berjalan secara SYNC
     */
    @Autowired
    private TransactionOperations transactionOperations;

    /*
    -- Manual Programmatic Transaction (Platform Transaction Manager) --
    - Untuk kasus sederhana dan MANUAL jika kita perlu "ASYNC" Transactional
    - Karena @Transactional berjalan secara SYNC
     */
    @Autowired
    private PlatformTransactionManager platformTransactionManager;



    /*
        -- @Transactional Annotation --
        - Bisa disebut juga dengan "Declarative Transaction"
        - Tidak perlu membuat "Begin" dan "Commit" secara manual

        RULES:
        1. Harus digunakan di object / class yang berbeda
        - Misalnya ini dibuat di CategoryService.create(), harus dipanggil di luar object CategoryService agar @Transactional berjalan
        - Jika dipanggil di object yang sama, tidak akan berfungsi, karena @Transactional itu dibungkus oleh AOP seperti proxy
     */

    /*
        -- @Transactional Propagation --
        - Berfungsi jika ServiceA menggunakan @Transactional, dan di dalam methodnya memanggil ServiceB yang menggunakan @Transactional juga
        - Jadi seperti Nested @Transactional, ada @Transactional di dalam @Transactional yang lain

        1. REQUIRED        : Default. Gunakan transaksi yang sedang berjalan, atau buat baru jika tidak ada.
                        -> Cocok untuk use case umum.

        2. REQUIRES_NEW    : Selalu buat transaksi baru. Suspend transaksi sebelumnya.
                            -> Untuk operasi terpisah seperti email, audit log, dsb.

        3. MANDATORY       : Harus dipanggil dalam transaksi. Kalau tidak ada transaksi, throw Exception.
                            -> Pastikan konteks atomic sudah disiapkan di outer method.

        4. SUPPORTS        : Ikut transaksi kalau ada, kalau tidak ya jalan tanpa transaksi.
                            -> Untuk method yang fleksibel, seperti query read-only.

        5. NOT_SUPPORTED   : Suspend transaksi jika ada, dan jalankan non-transactional.
                            -> Untuk proses yang memakan waktu atau tidak perlu transactional.

        6. NEVER           : Tidak boleh ada transaksi. Kalau ada transaksi berjalan, throw Exception.
                            -> Untuk logic yang tidak boleh di-rollback.

        7. NESTED          : Seperti REQUIRED, tapi buat savepoint. Inner rollback tidak membatalkan outer.
                            -> Bagus untuk proses batch atau partial rollback.
     */
    @Transactional
    public void create(){
        for (int i = 0; i < 5; i++) {
            Category category = new Category();
            category.setName("Category-" + i);
            categoryRepository.save(category);
        }

        throw new RuntimeException("Ups, Rollback please!");
    }

    public void testCreate(){
        create();
    }

    public void error(){
        throw new RuntimeException("Error, Rollback please!");
    }


    // Manual Transaction for ASYNC Method
    public void createWithTransactionOperations(){
        transactionOperations.executeWithoutResult((status) -> {
            for (int i = 0; i < 5; i++) {
                Category category = new Category();
                category.setName("Category-" + i);
                categoryRepository.save(category);
            }
            error();
        });
    }


    // "Literally" Manual Transaction
    public void createWithPlatformTransactionManager(){
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();

        // Set manual configuration
        defaultTransactionDefinition.setTimeout(10);
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        // Get transaction status from existing transaction
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(defaultTransactionDefinition);

        try{
            for (int i = 0; i < 5; i++) {
                Category category = new Category();
                category.setName("Category-" + i);
                categoryRepository.save(category);
            }

            error(); // Call Exception

            // Manual Commit
            platformTransactionManager.commit(transactionStatus);
        }catch (Throwable throwable){

            // Manual Rollback
            platformTransactionManager.rollback(transactionStatus);

            // Throw Exception
            throw  throwable;
        }

    }
}
