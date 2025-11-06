package dikki_dev.learn_spring_data_jpa.services;

import dikki_dev.learn_spring_data_jpa.entities.Category;
import dikki_dev.learn_spring_data_jpa.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

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
}
