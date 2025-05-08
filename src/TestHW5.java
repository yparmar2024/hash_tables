import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class TestHW5 {
    @Test
    public void test_Step1_ContainEmpty() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        Assertions.assertFalse(ht.contains(0));
        Assertions.assertFalse(ht.contains(5));
    }

    @Test
    public void test_Step1_InsertChanges() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        Assertions.assertFalse(ht.contains(0));
        ht.insert(0);
        Assertions.assertTrue(ht.contains(0));
    }

    @Test
    public void test_Step1_InsertDodgesCollisions() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        Assertions.assertFalse(ht.contains(0));
        ht.insert(8);
        Assertions.assertFalse(ht.contains(0));
        Assertions.assertTrue(ht.contains(8));
    }

    @Test
    public void test_Step1_Errors() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        Assertions.assertThrows(NullPointerException.class, () -> ht.contains(null));
        Assertions.assertThrows(NullPointerException.class, () -> ht.insert(null));
        Assertions.assertDoesNotThrow(() -> {
            for (int i = 0; i < 8; i++) {
                ht.insert(i);
            }
        });
        Assertions.assertThrows(OutOfSpaceException.class, () -> ht.insert(9));
        Assertions.assertDoesNotThrow(() -> ht.insert(0));
    }

    @Test
    public void test_Step1_NoDuplicates() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        Assertions.assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                ht.insert(0);
            }
        });
    }

    @Test
    public void test_Step1_OtherTypes() {
        LinearHashTable<String> ht = new LinearHashTable<>(50);
        Assertions.assertFalse(ht.contains("Hello"));
        ht.insert("Hello");
        ht.insert("World");
        Assertions.assertTrue(ht.contains("Hello"));
        Assertions.assertFalse(ht.contains("Other"));
    }

    @Test
    public void test_Step2_AccessCountEmpty() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        Assertions.assertEquals(0, ht.accessCount());
    }

    @Test
    public void test_Step2_AccessCountBasic() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        ht.insert(0);
        ht.insert(1);
        ht.insert(2);
        Assertions.assertEquals(3, ht.accessCount());
    }

    @Test
    public void test_Step2_AccessCountCollisions() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        ht.insert(0);
        ht.insert(1);
        ht.insert(2);
        ht.insert(8); // This makes 4 accesses: 0, 1, 2, 3.
        Assertions.assertEquals(7, ht.accessCount());
    }

    @Test
    public void test_Step2_ResetAccesses() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        ht.insert(0);
        ht.insert(1);
        ht.insert(2);
        Assertions.assertEquals(3, ht.accessCount());
        ht.resetAccesses();
        Assertions.assertEquals(0, ht.accessCount());
        ht.insert(5);
        Assertions.assertEquals(1, ht.accessCount());
    }

    @Test
    public void test_Step2_AccessCountContains() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        ht.insert(0);
        ht.insert(1);
        ht.insert(2);
        Assertions.assertEquals(3, ht.accessCount());
        ht.contains(5); // 1 access
        ht.contains(0); // 1 access
        ht.contains(8); // 4 accesses
        Assertions.assertEquals(9, ht.accessCount());
    }

    @Test
    public void test_Step2_AccessCountStopEarly() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        ht.insert(0);
        ht.insert(8);
        ht.insert(16);
        ht.insert(24);
        ht.resetAccesses();
        ht.contains(8); // Only 2 accesses. Stops at 8.
        Assertions.assertEquals(2, ht.accessCount());
        ht.insert(16); // Only 3 accesses. Does not go beyond 16.
        Assertions.assertEquals(5, ht.accessCount());
    }

    @Test
    public void test_Step2_AccessCountComplex() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        ht.insert(0); // 1
        ht.insert(8); // 2
        ht.contains(80); // 3
        Assertions.assertEquals(6, ht.accessCount());
        ht.insert(1); // 2
        ht.contains(80); // 4
        Assertions.assertEquals(12, ht.accessCount());
        ht.insert(16); // 4
        ht.insert(5); // 1
        ht.contains(24); // 5
        ht.contains(16); // 4
        Assertions.assertEquals(26, ht.accessCount());
    }

    @Test
    public void test_Step3_SizeLoadCapacityEmpty() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        Assertions.assertEquals(8, ht.capacity());
        Assertions.assertEquals(0, ht.size());
        Assertions.assertEquals(0, ht.load());
    }

    @Test
    public void test_Step3_SizeLoadCapacityInsert() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        ht.insert(0);
        ht.insert(2);
        Assertions.assertEquals(8, ht.capacity());
        Assertions.assertEquals(2, ht.size());
        Assertions.assertEquals(2, ht.load());
    }

    @Test
    public void test_Step3_SizeLoadCapacityInsertRepeated() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        ht.insert(0);
        ht.insert(8);
        ht.insert(0);
        ht.insert(0);
        Assertions.assertEquals(8, ht.capacity());
        Assertions.assertEquals(2, ht.size());
        Assertions.assertEquals(2, ht.load());
        ht.insert(0);
        ht.insert(0);
        Assertions.assertEquals(8, ht.capacity());
        Assertions.assertEquals(2, ht.size());
        Assertions.assertEquals(2, ht.load());
    }

    @Test
    public void test_Step4_DeleteEmpty() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        ht.delete(0);
        Assertions.assertEquals(0, ht.size());
        Assertions.assertEquals(0, ht.load());
        Assertions.assertFalse(ht.contains(0));
    }

    @Test
    public void test_Step4_DeleteBasic() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        ht.insert(0);
        Assertions.assertEquals(1, ht.size());
        Assertions.assertEquals(1, ht.load());
        Assertions.assertTrue(ht.contains(0));
        ht.delete(0);
        Assertions.assertEquals(0, ht.size());
        Assertions.assertEquals(1, ht.load());
        Assertions.assertFalse(ht.contains(0));
    }

    @Test
    public void test_Step4_DeleteDuplicates() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        ht.insert(0);
        ht.insert(0);
        ht.insert(0);
        Assertions.assertEquals(1, ht.size());
        Assertions.assertEquals(1, ht.load());
        Assertions.assertTrue(ht.contains(0));
        ht.delete(0);
        Assertions.assertEquals(0, ht.size());
        Assertions.assertEquals(1, ht.load());
        Assertions.assertFalse(ht.contains(0));
        ht.delete(0);
        Assertions.assertEquals(0, ht.size());
        Assertions.assertEquals(1, ht.load());
        Assertions.assertFalse(ht.contains(0));
    }

    @Test
    public void test_Step4_DeleteAccesses() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        ht.insert(0);
        ht.insert(9);
        ht.insert(8);
        ht.resetAccesses();
        ht.delete(16); // 4
        Assertions.assertEquals(4, ht.accessCount());
        ht.resetAccesses();
        ht.delete(8); // 3
        Assertions.assertEquals(3, ht.accessCount());
    }

    @Test
    public void test_Step4_DeleteSkipped() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        ht.insert(0);
        ht.insert(9);
        ht.insert(8);
        ht.delete(9);
        ht.resetAccesses();
        Assertions.assertTrue(ht.contains(8));
        Assertions.assertEquals(3, ht.accessCount());
    }

    @Test
    public void test_Step4_DeleteReinsert() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        ht.insert(0);
        ht.insert(9);
        ht.insert(8);
        ht.delete(9);
        ht.resetAccesses();
        ht.insert(16); // 4 to check, but inserts at 2.
        Assertions.assertEquals(4, ht.accessCount());
        Assertions.assertTrue(ht.contains(16));
        Assertions.assertEquals(6, ht.accessCount());
    }

    @Test
    public void test_Step4_DeleteReinsertLoads() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        ht.insert(0);
        ht.insert(9);
        ht.insert(8);
        Assertions.assertEquals(3, ht.size());
        Assertions.assertEquals(3, ht.load());
        ht.delete(9);
        Assertions.assertEquals(2, ht.size());
        Assertions.assertEquals(3, ht.load());
        ht.insert(5); // Inserts over null.
        Assertions.assertEquals(3, ht.size());
        Assertions.assertEquals(4, ht.load());
        ht.insert(16); // Inserts over DELETED.
        Assertions.assertEquals(4, ht.size());
        Assertions.assertEquals(4, ht.load());
    }

    @Test
    public void test_Step4_DeleteError() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        Assertions.assertThrows(NullPointerException.class, () -> ht.delete(null));
    }

    @Test
    public void test_Step4_DeleteCreatesRoom() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        for (int i = 0; i < 8; i++) {
            ht.insert(i*8);
        }
        ht.delete(48);
        Assertions.assertEquals(7, ht.size());
        Assertions.assertEquals(8, ht.load());
        Assertions.assertDoesNotThrow(() -> ht.insert(800));
        Assertions.assertTrue(ht.contains(800));
    }

    @Test
    public void test_Step4_DeleteMissesOnFull() {
        LinearHashTable<Integer> ht = new LinearHashTable<>(8);
        for (int i = 0; i < 8; i++) {
            ht.insert(i*8);
        }
        Assertions.assertDoesNotThrow(() -> ht.delete(42));
    }

    @Test
    public void test_Step5_Empty() {
        QuadraticHashTable<Integer> ht = new QuadraticHashTable<>(8);
        Assertions.assertEquals(8, ht.capacity());
        Assertions.assertEquals(0, ht.size());
        Assertions.assertEquals(0, ht.load());
        Assertions.assertEquals(0, ht.accessCount());
        Assertions.assertFalse(ht.contains(0));
        Assertions.assertEquals(1, ht.accessCount());
    }

    @Test
    public void test_Step5_BasicInsertDelete() {
        QuadraticHashTable<Integer> ht = new QuadraticHashTable<>(8);
        ht.insert(0);
        Assertions.assertEquals(1, ht.accessCount());
        Assertions.assertEquals(1, ht.size());
        Assertions.assertEquals(1, ht.load());
        Assertions.assertTrue(ht.contains(0));
        Assertions.assertFalse(ht.contains(8));
        ht.resetAccesses();
        ht.insert(8);
        Assertions.assertEquals(2, ht.accessCount());
        Assertions.assertEquals(2, ht.size());
        Assertions.assertEquals(2, ht.load());
        Assertions.assertTrue(ht.contains(0));
        Assertions.assertTrue(ht.contains(8));
        ht.resetAccesses();
        ht.delete(0);
        Assertions.assertEquals(1, ht.accessCount());
        Assertions.assertEquals(1, ht.size());
        Assertions.assertEquals(2, ht.load());
        Assertions.assertFalse(ht.contains(0));
        Assertions.assertTrue(ht.contains(8));
        ht.resetAccesses();
        ht.insert(16);
        Assertions.assertEquals(3, ht.accessCount());
        Assertions.assertEquals(2, ht.size());
        Assertions.assertEquals(2, ht.load());
        Assertions.assertFalse(ht.contains(0));
        Assertions.assertTrue(ht.contains(8));
        Assertions.assertTrue(ht.contains(16));
    }

    @Test
    public void test_Step5_InsertJumps() {
        QuadraticHashTable<Integer> ht = new QuadraticHashTable<>(25);
        ht.insert(0);
        ht.insert(1);
        ht.insert(3);
        ht.insert(6);
        ht.insert(10);
        ht.resetAccesses();
        Assertions.assertFalse(ht.contains(25));
        Assertions.assertEquals(6, ht.accessCount());
    }

    @Test
    public void test_Step5_OutOfSpace() {
        QuadraticHashTable<Integer> ht = new QuadraticHashTable<>(7);
        ht.insert(0);
        ht.insert(1);
        ht.insert(3);
        ht.insert(6);
        Assertions.assertThrows(OutOfSpaceException.class, () -> ht.insert(7));
    }
}
