void main() {
        int n = 5;
        int []a = {10, 30, 7, n, 64};
        int i = 1;
        int min = a[0];
        while (i < 5) {
            if(a[i] < min) {
                min = a[i];
            }
            i = i + 1;
        }

        System.out.println(min);
    }
